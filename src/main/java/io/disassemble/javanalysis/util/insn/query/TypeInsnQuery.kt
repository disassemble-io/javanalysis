package io.disassemble.javanalysis.util.insn.query

import io.disassemble.javanalysis.insn.TypeInsn

/**
 * A class representing a query for an [TypeInsn].
 * Used for filtering in lists.
 */
class TypeInsnQuery : InsnQuery<TypeInsn>() {

    /**
     * Adds a condition to [InsnQuery.conditions] in which [TypeInsn.type] must match the given value.
     *
     * @param type The value to match against [TypeInsn.type].
     *
     * @return The same [TypeInsnQuery] that this method was called from.
     */
    fun type(type: String): TypeInsnQuery {
        conditions.add { it.type == type }
        return this
    }
}