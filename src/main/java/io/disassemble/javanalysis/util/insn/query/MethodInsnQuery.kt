package io.disassemble.javanalysis.util.insn.query
import io.disassemble.javanalysis.insn.MethodInsn

/**
 * A class representing a query for an [MethodInsn].
 * Used for filtering in lists.
 */
class MethodInsnQuery : InsnQuery<MethodInsn>() {

    /**
     * Adds a condition to [InsnQuery.conditions] in which [MethodInsn.parent] must match the given value.
     *
     * @param parent The value to match against [MethodInsn.parent].
     *
     * @return The same [MethodInsnQuery] that this method was called from.
     */
    fun parent(parent: String): MethodInsnQuery {
        conditions.add { it.parent == parent }
        return this
    }

    /**
     * Adds a condition to [InsnQuery.conditions] in which [MethodInsn.name] must match the given value.
     *
     * @param methodName The value to match against [MethodInsn.name].
     *
     * @return The same [MethodInsnQuery] that this method was called from.
     */
    fun methodName(methodName: String): MethodInsnQuery {
        conditions.add { it.name == methodName }
        return this
    }

    /**
     * Adds a condition to [InsnQuery.conditions] in which [MethodInsn.desc] must match the given value.
     *
     * @param desc The value to match against [MethodInsn.desc].
     *
     * @return The same [MethodInsnQuery] that this method was called from.
     */
    fun desc(desc: String) : MethodInsnQuery {
        conditions.add { it.desc == desc }
        return this
    }
}