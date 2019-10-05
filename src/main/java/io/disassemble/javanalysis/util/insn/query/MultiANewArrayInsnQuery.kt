package io.disassemble.javanalysis.util.insn.query

import io.disassemble.javanalysis.insn.MultiANewArrayInsn

/**
 * A class representing a query for an [MultiANewArrayInsn].
 * Used for filtering in lists.
 */
class MultiANewArrayInsnQuery : InsnQuery<MultiANewArrayInsn>() {

    /**
     * Adds a condition to [InsnQuery.conditions] in which [MultiANewArrayInsn.type] must match the given value.
     *
     * @param type The value to match against [MultiANewArrayInsn.type].
     *
     * @return The same [MultiANewArrayInsnQuery] that this method was called from.
     */
    fun type(type: String): MultiANewArrayInsnQuery {
        conditions.add { it.type == type }
        return this
    }

    /**
     * Adds a condition to [InsnQuery.conditions] in which [MultiANewArrayInsn.dimensions] must match the given value.
     *
     * @param dimensions The value to match against [MultiANewArrayInsn.dimensions].
     *
     * @return The same [MultiANewArrayInsnQuery] that this method was called from.
     */
    fun dimensions(dimensions: Int): MultiANewArrayInsnQuery {
        conditions.add { it.dimensions == dimensions }
        return this
    }
}