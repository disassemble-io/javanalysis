package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.CtMethodNode

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class VarInsn(
        owner: CtMethodNode,
        index: Int,
        opcode: Int,
        protected var `var`: Int
) : CtInsn(owner, index, opcode) {

    fun `var`(): Int {
        return `var`
    }
}
