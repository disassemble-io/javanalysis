package io.disassemble.javanalysis.util.insn.query

import io.disassemble.javanalysis.insn.IncrementInsn

/**
 * A class representing a query for an [IncrementInsn].
 * Used for filtering in lists.
 */
class IncrementInsnQuery : InsnQuery<IncrementInsn>() {

    /**
     * Adds a condition to [InsnQuery.conditions] in which [IncrementInsn.variable] must match the given value.
     *
     * @param variable The value to match against [IncrementInsn.variable].
     *
     * @return The same [IncrementInsnQuery] that this method was called from.
     */
    fun variable(variable: Int): IncrementInsnQuery {
        conditions.add { it.variable == variable }
        return this
    }

    /**
     * Adds a condition to [InsnQuery.conditions] in which [IncrementInsn.increment] must match the given value.
     *
     * @param increment The value to match against [IncrementInsn.increment].
     *
     * @return The same [IncrementInsnQuery] that this method was called from.
     */
    fun increment(increment: Int): IncrementInsnQuery {
        conditions.add { it.increment == increment }
        return this
    }
}