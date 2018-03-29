package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.CtMethodNode

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class IncrementInsn(
        owner: CtMethodNode,
        index: Int,
        opcode: Int,
        protected var `var`: Int,
        protected var increment: Int
) : CtInsn(owner, index, opcode) {

    fun `var`(): Int {
        return `var`
    }

    fun increment(): Int {
        return increment
    }
}
