package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.CtMethodNode

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class LookupSwitchInsn(
        owner: CtMethodNode,
        index: Int,
        opcode: Int,
        protected var defaultIndex: Int,
        protected var keys: IntArray,
        protected var indices: IntArray
) : CtInsn(owner, index, opcode) {

    fun defaultIndex(): Int {
        return defaultIndex
    }

    fun size(): Int {
        return keys.size
    }

    fun keys(): IntArray {
        return keys
    }

    fun indices(): IntArray {
        return indices
    }
}
