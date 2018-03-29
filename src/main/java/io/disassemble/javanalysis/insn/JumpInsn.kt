package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.CtMethodNode

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class JumpInsn(
        owner: CtMethodNode,
        index: Int,
        opcode: Int,
        protected var target: Int
) : CtInsn(owner, index, opcode) {

    fun target(): Int {
        return target
    }
}
