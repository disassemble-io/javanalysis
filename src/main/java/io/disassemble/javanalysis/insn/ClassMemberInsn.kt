package io.disassemble.javanalysis.insn

import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
open class ClassMemberInsn(
        owner: CtMethod,
        index: Int,
        opcode: Int,
        protected var parent: String,
        protected var name: String,
        protected var desc: String
) : CtInsn(owner, index, opcode) {

    fun parent(): String {
        return parent
    }

    fun name(): String {
        return name
    }

    fun desc(): String {
        return desc
    }

    open fun key(): String {
        return "$parent.$name$desc"
    }
}
