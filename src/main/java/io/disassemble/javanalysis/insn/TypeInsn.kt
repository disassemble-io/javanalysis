package io.disassemble.javanalysis.insn

import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
open class TypeInsn(
        owner: CtMethod,
        index: Int,
        opcode: Int,
        protected var type: String
) : CtInsn(owner, index, opcode) {

    fun type(): String {
        return type
    }
}
