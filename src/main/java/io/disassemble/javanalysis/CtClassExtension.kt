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