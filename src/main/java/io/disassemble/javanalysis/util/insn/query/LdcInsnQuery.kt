package io.disassemble.javanalysis.util.insn.query

import io.disassemble.javanalysis.insn.LdcInsn

/**
 * A class representing a query for an [LdcInsn].
 * Used for filtering in lists.
 */
class LdcInsnQuery : InsnQuery<LdcInsn>() {

    /**
     * Adds a condition to [InsnQuery.conditions] in which [LdcInsn.cst] must match the given value.
     *
     * @param cst The value to match against [LdcInsn.cst].
     *
     * @return The same [LdcInsnQuery] that this method was called from.
     */
    fun cst(cst: Any): LdcInsnQuery {
        conditions.add { it.cst == cst }
        return this
    }
}