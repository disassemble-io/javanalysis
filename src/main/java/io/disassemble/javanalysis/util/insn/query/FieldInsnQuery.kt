package io.disassemble.javanalysis.util.insn.query

import io.disassemble.javanalysis.insn.FieldInsn

/**
 * A class representing a query for an [FieldInsn].
 * Used for filtering in lists.
 */
class FieldInsnQuery : InsnQuery<FieldInsn>() {

    /**
     * Adds a condition to [InsnQuery.conditions] in which [FieldInsn.parent] must match the given value.
     *
     * @param parent The value to match against [FieldInsn.parent].
     *
     * @return The same [FieldInsnQuery] that this method was called from.
     */
    fun parent(parent: String): FieldInsnQuery {
        conditions.add { it.parent == parent }
        return this
    }

    /**
     * Adds a condition to [InsnQuery.conditions] in which [FieldInsn.name] must match the given value.
     *
     * @param fieldName The value to match against [FieldInsn.name].
     *
     * @return The same [FieldInsnQuery] that this method was called from.
     */
    fun fieldName(fieldName: String): FieldInsnQuery {
        conditions.add { it.name == fieldName }
        return this
    }

    /**
     * Adds a condition to [InsnQuery.conditions] in which [FieldInsn.desc] must match the given value.
     *
     * @param desc The value to match against [FieldInsn.desc].
     *
     * @return The same [FieldInsnQuery] that this method was called from.
     */
    fun desc(desc: String) : FieldInsnQuery {
        conditions.add { it.desc == desc }
        return this
    }
}