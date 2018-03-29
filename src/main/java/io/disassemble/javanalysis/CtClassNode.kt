package io.disassemble.javanalysis

import javassist.CtClass
import java.util.*

/**
 * @author Tyler Sedlar
 * @since 5/19/2017
 */
class CtClassNode(private val source: CtClass) {
    private val methods = ArrayList<CtMethodNode>()

    init {
        source.declaredMethods.mapTo(methods) { CtMethodNode(it, this) }
    }

    fun source(): CtClass {
        return source
    }

    fun name(): String {
        return source.name
    }

    fun methods(): List<CtMethodNode> {
        return methods
    }
}
