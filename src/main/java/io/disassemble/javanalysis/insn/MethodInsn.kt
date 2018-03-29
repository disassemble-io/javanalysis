package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.CtMethodNode

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class MethodInsn(
        owner: CtMethodNode,
        index: Int,
        opcode: Int,
        parent: String,
        name: String,
        desc: String
) : ClassMemberInsn(owner, index, opcode, parent, name, desc)
