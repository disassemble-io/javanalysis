package io.disassemble.javanalysis.util.insn.query

import io.disassemble.javanalysis.insn.VarInsn

/**
 * A class representing a query for an [VarInsn].
 * Used for filtering in lists.
 */
class VarInsnQuery : InsnQuery<VarInsn>() {

    /**
     * Adds a condition to [InsnQuery.conditions] in which [VarInsn.variable] must match the given value.
     *
     * @param variable The value to match against [VarInsn.variable].
     *
     * @return The same [VarInsnQuery] that this method was called from.
     */
    fun variable(variable: Int): VarInsnQuery {
        conditions.add { it.variable == variable }
        return this
    }
}