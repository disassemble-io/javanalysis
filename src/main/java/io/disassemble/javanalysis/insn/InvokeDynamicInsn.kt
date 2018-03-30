package io.disassemble.javanalysis.insn

import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class InvokeDynamicInsn(
        owner: CtMethod,
        index: Int,
        opcode: Int,
        val nameAndType: Int,
        val type: String,
        val bootstrap: Int
) : CtInsn(owner, index, opcode)