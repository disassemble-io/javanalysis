package io.disassemble.javanalysis.insn

import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class IntInsn(
        owner: CtMethod,
        index: Int,
        opcode: Int,
        val operand: Int
) : CtInsn(owner, index, opcode)