package io.disassemble.javanalysis

import javassist.CtClass
import javassist.CtConstructor
import javassist.CtMethod
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.addAll
import kotlin.collections.contains
import kotlin.collections.forEach
import kotlin.collections.set

/**
 * A class to add extension methods to CtClass
 *
 * @author Tyler Sedlar
 * @since 5/19/2017
 */

/**
 * A map of method [CtClass.staticInitializer], where the key is [CtClass.hash].
 */
private val clinitMap: MutableMap<Int, CtMethod> = HashMap()

/**
 * A map of [CtClass.getConstructors], where the key is [CtClass.hash].
 */
private val constructorMap: MutableMap<Int, List<CtMethod>> = HashMap()

/**
 * Gets a unique hash for this class.
 */
val CtClass.hash: Int
    get() {
        return System.identityHashCode(this)
    }

/**
 * Gets a list of <clinit> for this [CtClass].
 */
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

/**
 * Gets a list of constructors for this [CtClass].
 */
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

/**
 * Gets all of the [CtMethod] within this [CtClass].
 */
val CtClass.allMethods: List<CtMethod>
    get() {
        val list: MutableList<CtMethod> = ArrayList()
        staticInitializer?.let { list.add(it) }
        list.addAll(inits)
        list.addAll(methods)
        return list
    }

/**
 * Gets all declared [CtMethod] within this [CtClass].
 */
val CtClass.allDeclaredMethods: List<CtMethod>
    get() {
        val list: MutableList<CtMethod> = ArrayList()
        staticInitializer?.let { list.add(it) }
        list.addAll(inits)
        list.addAll(declaredMethods)
        return list
    }