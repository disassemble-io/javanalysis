package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.CtMethodNode

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class IntInsn(
        owner: CtMethodNode,
        index: Int,
        opcode: Int,
        operand: Int
) : CtInsn(owner, index, opcode) {

    protected var operand = -1

    init {
        this.operand = operand
    }

    fun operand(): Int {
        return operand
    }
}
