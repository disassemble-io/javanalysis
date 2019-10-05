package io.disassemble.javanalysis.util.insn.query

import io.disassemble.javanalysis.insn.InvokeDynamicInsn

/**
 * A class representing a query for an [InvokeDynamicInsn].
 * Used for filtering in lists.
 */
class InvokeDynamicInsnQuery : InsnQuery<InvokeDynamicInsn>() {

    /**
     * Adds a condition to [InsnQuery.conditions] in which [InvokeDynamicInsn.type] must match the given value.
     *
     * @param type The value to match against [InvokeDynamicInsn.type].
     *
     * @return The same [InvokeDynamicInsnQuery] that this method was called from.
     */
    fun type(type: String): InvokeDynamicInsnQuery {
        conditions.add { it.type == type }
        return this
    }

    /**
     * Adds a condition to [InsnQuery.conditions] in which [InvokeDynamicInsn.bootstrap] must match the given value.
     *
     * @param bootstrap The value to match against [InvokeDynamicInsn.bootstrap].
     *
     * @return The same [InvokeDynamicInsnQuery] that this method was called from.
     */
    fun bootstrap(bootstrap: Int): InvokeDynamicInsnQuery {
        conditions.add { it.bootstrap == bootstrap }
        return this
    }
}