package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.CtMethodNode

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
open class TypeInsn(
        owner: CtMethodNode,
        index: Int,
        opcode: Int,
        protected var type: String
) : CtInsn(owner, index, opcode) {

    fun type(): String {
        return type
    }
}
