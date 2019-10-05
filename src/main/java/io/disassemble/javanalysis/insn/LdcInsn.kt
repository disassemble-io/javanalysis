package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import io.disassemble.javanalysis.info
import io.disassemble.javanalysis.pool
import javassist.CtMethod
import javassist.bytecode.Opcode

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */

/**
 * A class representing a constant instruction.
 *
 * @param owner The [CtMethod] that this instruction is a part of.
 * @param index The index of this instruction in [CtMethod].
 */
class LdcInsn(
        owner: CtMethod,
        index: Int
) : CtInsn(owner, index) {

    private var poolIndex: Int
        get() {
            return if (opcode == Opcode.LDC) {
                owner.code.iterator().byteAt(index + 1)
            } else {
                owner.code.iterator().u16bitAt(index + 1)
            }
        }
        set(value) {
            if (opcode == Opcode.LDC) {
                owner.code.iterator().writeByte(value, index + 1)
            } else {
                owner.code.iterator().write16bit(value, index + 1)
            }
        }

    /**
     * The type of constant that this instruction is
     */
    val tag: Int
        get() = owner.info.constPool.getTag(poolIndex)

    /**
     * The constant value
     */
    val cst: Any
        get() = owner.pool.getLdcValue(poolIndex)

    /**
     * Sets this constant to a new [String] value.
     *
     * @param value The [String] value to be set to.
     */
    fun setString(value: String) {
        poolIndex = owner.pool.addStringInfo(value)
    }

    /**
     * Sets this constant to a new [String] value.
     *
     * @param value The [String] value to be set to.
     */
    fun setUtf8(value: String) {
        poolIndex = owner.pool.addUtf8Info(value)
    }

    /**
     * Sets this constant to a new [Int] value.
     *
     * @param value The [Int] value to be set to.
     */
    fun setInt(value: Int) {
        poolIndex = owner.pool.addIntegerInfo(value)
    }

    /**
     * Sets this constant to a new [Long] value.
     *
     * @param value The [Long] value to be set to.
     */
    fun setLong(value: Long) {
        poolIndex = owner.pool.addLongInfo(value)
    }

    /**
     * Sets this constant to a new [Double] value.
     *
     * @param value The [Double] value to be set to.
     */
    fun setDouble(value: Double) {
        poolIndex = owner.pool.addDoubleInfo(value)
    }

    /**
     * Sets this constant to a new [Float] value.
     *
     * @param value The [Float] value to be set to.
     */
    fun setFloat(value: Float) {
        poolIndex = owner.pool.addFloatInfo(value)
    }
}
