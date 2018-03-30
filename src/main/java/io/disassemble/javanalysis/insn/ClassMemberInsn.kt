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
        val parent: String,
        val name: String,
        val desc: String
) : CtInsn(owner, index, opcode) {

    open val key: String
        get() = "$parent.$name$desc"
}
