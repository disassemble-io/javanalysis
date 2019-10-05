package io.disassemble.javanalysis.util.insn.query

import io.disassemble.javanalysis.insn.IntInsn

/**
 * A class representing a query for an [IntInsn].
 * Used for filtering in lists.
 */
class IntInsnQuery : InsnQuery<IntInsn>() {

    /**
     * Adds a condition to [InsnQuery.conditions] in which [IntInsn.operand] must match the given value.
     *
     * @param operand The value to match against [IntInsn.operand].
     *
     * @return The same [IntInsnQuery] that this method was called from.
     */
    fun operand(operand: Int): IntInsnQuery {
        conditions.add { it.operand == operand }
        return this
    }
}