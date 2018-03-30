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
        val dimensions: Int
) : TypeInsn(owner, index, opcode, type)