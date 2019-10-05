package io.disassemble.javanalysis.util.insn.query

import io.disassemble.javanalysis.insn.JumpInsn

/**
 * A class representing a query for an [JumpInsn].
 * Used for filtering in lists.
 */
class JumpInsnQuery : InsnQuery<JumpInsn>() {

    /**
     * Adds a condition to [InsnQuery.conditions] in which [JumpInsn.target] must match the given value.
     *
     * @param target The value to match against [JumpInsn.target].
     *
     * @return The same [JumpInsnQuery] that this method was called from.
     */
    fun target(target: Int): JumpInsnQuery {
        conditions.add { it.target == target }
        return this
    }
}