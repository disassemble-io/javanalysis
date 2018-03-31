package io.disassemble.javanalysis

import javassist.CtClass
import javassist.CtConstructor
import javassist.CtMethod
import java.util.HashMap

/**
 * @author Tyler Sedlar
 * @since 5/19/2017
 *
 * A class to add extension methods to CtClass
 */

private val clinitMap: MutableMap<Int, CtMethod> = HashMap()
private val constructorMap: MutableMap<Int, List<CtMethod>> = HashMap()

val CtClass.hash: Int
    get() {
        return System.identityHashCode(this)
    }

val CtClass.staticInitializer: CtMethod?
    get() {
        if (hash in clinitMap) {
            return clinitMap[hash]!!
        } else {
            this.declaredBehaviors.forEach {
                if (it is CtConstructor && it.name == "<clinit>") {
                    val method = CtMethod.make(it.methodInfo, it.declaringClass)
                    clinitMap[hash] = method
                    return method
                }
            }
        }
        return null
    }

val CtClass.inits: List<CtMethod>
    get() {
        return if (hash in constructorMap) {
            constructorMap[hash]!!
        } else {
            val list: MutableList<CtMethod> = ArrayList()
            this.constructors.forEach {
                list.add(CtMethod.make(it.methodInfo, this))
            }
            constructorMap[hash] = list
            list
        }
    }

val CtClass.allMethods: List<CtMethod>
    get() {
        val list: MutableList<CtMethod> = ArrayList()
        staticInitializer?.let { list.add(it) }
        list.addAll(inits)
        list.addAll(methods)
        return list
    }