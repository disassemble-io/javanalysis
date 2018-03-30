package io.disassemble.javanalysis.insn

import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class MultiANewArrayInsn(
        owner: CtMethod,
        index: Int,
        opcode: Int,
        type: String,
        protected var dimensions: Int
) : TypeInsn(owner, index, opcode, type) {

    fun dimensions(): Int {
        return dimensions
    }
}
