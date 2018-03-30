package io.disassemble.javanalysis.insn

import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class VarInsn(
        owner: CtMethod,
        index: Int,
        opcode: Int,
        val variable: Int
) : CtInsn(owner, index, opcode)