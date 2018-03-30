package io.disassemble.javanalysis.insn

import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class TableSwitchInsn(
        owner: CtMethod,
        index: Int,
        opcode: Int,
        val low: Int,
        val high: Int,
        val defaultIndex: Int,
        val indices: IntArray
) : CtInsn(owner, index, opcode)