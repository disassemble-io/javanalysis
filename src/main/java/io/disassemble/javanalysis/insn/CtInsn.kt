package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import io.disassemble.javanalysis.indexOf
import io.disassemble.javanalysis.info
import io.disassemble.javanalysis.line
import io.disassemble.javanalysis.util.InsnUtil
import javassist.CtMethod
import javassist.bytecode.Mnemonic

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */

/**
 * A class representing a bytecode instruction.
 *
 * @param owner The [CtMethod] that this instruction is a part of.
 * @param index The index of this instruction in [CtMethod].
 */
open class CtInsn(val owner: CtMethod, var index: Int) {

    /**
     * The [CtInsn] that appears before this instruction.
     */
    var previous: CtInsn? = null

    /**
     * The [CtInsn] that appears after this instruction.
     */
    var next: CtInsn? = null

    /**
     * The opcode of this instruction.
     */
    var opcode: Int
        get() = owner.code.iterator().byteAt(index)
        set(value) = owner.code.iterator().writeByte(value, index)

    /**
     * The name of this instruction, appearing within [Mnemonic.OPCODE].
     */
    val opname: String
        get() = Mnemonic.OPCODE[opcode]

    /**
     * The line this instruction appears on.
     */
    val line: Int
        get() = owner.info.getLineNumber(index)

    /**
     * The line this instruction appears on, relative to its [CtMethod].
     */
    val relativeLine: Int
        get() = line - owner.line

    /**
     * The position of this instruction within its [CtMethod].
     */
    val position: Int
        get() = owner.indexOf(this)

    /**
     * A [String] representation of this instruction.
     */
    val verbose: String
        get() = InsnUtil.stringify(this)

    /**
     * Checks if the previous [CtInsn] is valid.
     *
     * @return true if the previous [CtInsn] is valid, otherwise false.
     */
    fun hasPrevious(): Boolean = previous != null

    /**
     * Checks if the next [CtInsn] is valid.
     *
     * @return true if the next [CtInsn] is valid, otherwise false.
     */
    operator fun hasNext(): Boolean = next != null

    /**
     * A basic [String] representation of this instruction.
     */
    override fun toString(): String = opname
}
