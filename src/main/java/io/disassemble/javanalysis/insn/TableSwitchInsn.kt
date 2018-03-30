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
        protected var low: Int,
        protected var high: Int,
        protected var defaultIndex: Int,
        protected var indices: IntArray
) : CtInsn(owner, index, opcode) {

    fun low(): Int {
        return low
    }

    fun high(): Int {
        return high
    }

    fun defaultIndex(): Int {
        return defaultIndex
    }

    fun indices(): IntArray {
        return indices
    }
}
