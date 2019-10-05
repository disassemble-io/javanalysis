package io.disassemble.javanalysis.util.insn.query

import io.disassemble.javanalysis.insn.WideInsn

/**
 * A class representing a query for an [WideInsn].
 * Used for filtering in lists.
 */
class WideInsnQuery : InsnQuery<WideInsn>() {

    /**
     * Adds a condition to [InsnQuery.conditions] in which [WideInsn.wideOp] must match the given value.
     *
     * @param wideOp The value to match against [WideInsn.wideOp].
     *
     * @return The same [WideInsnQuery] that this method was called from.
     */
    fun wideOp(wideOp: Int): WideInsnQuery {
        conditions.add { it.wideOp == wideOp }
        return this
    }

    /**
     * Adds a condition to [InsnQuery.conditions] in which [WideInsn.wideIndex] must match the given value.
     *
     * @param wideIndex The value to match against [WideInsn.wideIndex].
     *
     * @return The same [WideInsnQuery] that this method was called from.
     */
    fun wideIndex(wideIndex: Int): WideInsnQuery {
        conditions.add { it.wideIndex == wideIndex }
        return this
    }
}