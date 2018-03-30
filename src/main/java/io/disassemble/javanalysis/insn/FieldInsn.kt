package io.disassemble.javanalysis.insn

import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class FieldInsn(
        owner: CtMethod,
        index: Int,
        opcode: Int,
        parent: String,
        name: String,
        desc: String
) : ClassMemberInsn(owner, index, opcode, parent, name, desc) {

    override fun key(): String {
        return "$parent.$name"
    }
}
