package io.disassemble.javanalysis.insn

import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class WideInsn(
        owner: CtMethod,
        index: Int,
        opcode: Int,
        val wideOp: Int,
        val wideIndex: Int
) : CtInsn(owner, index, opcode)