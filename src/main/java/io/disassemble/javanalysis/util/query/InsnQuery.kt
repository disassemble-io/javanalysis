package io.disassemble.javanalysis.util.query

import io.disassemble.javanalysis.insn.CtInsn
import io.disassemble.javanalysis.insn.IntInsn
import javassist.bytecode.Opcode

/**
 * A class representing a query.
 * Used for filtering in lists.
 */
class InsnQuery {

    /**
     * Gives this query a name.
     * Used for putting into a map within [io.disassemble.javanalysis.InsnLine.find].
     */
    var name: String? = null

    /**
     * Sets the distance allowed to be traveled between queries within [io.disassemble.javanalysis.InsnLine.find].
     */
    var dist = 1

    /**
     * The list of conditions that must be met.
     * Used within [InsnQuery.check]
     */
    var conditions = ArrayList<(CtInsn) -> Boolean>()

    /**
     * Sets the value of [InsnQuery.name] and returns itself.
     *
     * @param name The value to set [InsnQuery.name] to.
     *
     * @return The same [InsnQuery] that this method was called from.
     */
    fun name(name: String): InsnQuery {
        this.name = name
        return this
    }

    /**
     * Sets the value of [InsnQuery.dist] and returns itself.
     *
     * @param dist The value to set [InsnQuery.dist] to.
     *
     * @return The same [InsnQuery] that this method was called from.
     */
    fun dist(dist: Int): InsnQuery {
        this.dist = dist
        return this
    }

    /**
     * Adds a condition to [InsnQuery.conditions] in which the matching [CtInsn] must be of one of the given opcodes.
     *
     * @param opcodes A list of opcodes to match.
     *
     * @return The same [InsnQuery] that this method was called from.
     */
    fun opcode(vararg opcodes: Int): InsnQuery {
        conditions.add { opcodes.contains(it.opcode) }
        return this
    }

    /**
     * Adds a condition to [InsnQuery.conditions] in which the matching [CtInsn] must be an [IntInsn] matching
     * the given operand.
     *
     * @param operand The value to match against [IntInsn.operand].
     *
     * @return The same [InsnQuery] that this method was called from.
     */
    fun int(operand: Int): InsnQuery {
        conditions.add { it is IntInsn && it.operand == operand }
        return this
    }

    /**
     * Checks if this [InsnQuery]'s conditions are met by the given [CtInsn].
     *
     * @param insn The [CtInsn] to check against.
     *
     * @return true if the [InsnQuery.conditions] were met, otherwise false.
     */
    fun check(insn: CtInsn): Boolean {
        for (condition in conditions) {
            if (!(condition(insn))) {
                return false
            }
        }
        return true
    }
}

/**
 * Creates an [InsnQuery] filter matching an [Opcode.SIPUSH] instruction.
 */
val sipush: InsnQuery
    get() = InsnQuery().apply {
        opcode(Opcode.SIPUSH)
    }