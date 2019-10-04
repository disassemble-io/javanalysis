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

    val tag: Int
        get() = owner.info.constPool.getTag(poolIndex)

    val cst: Any
        get() = owner.pool.getLdcValue(poolIndex)

    fun setString(value: String) {
        poolIndex = owner.pool.addStringInfo(value)
    }

    fun setUtf8(value: String) {
        poolIndex = owner.pool.addUtf8Info(value)
    }

    fun setInt(value: Int) {
        poolIndex = owner.pool.addIntegerInfo(value)
    }

    fun setLong(value: Long) {
        poolIndex = owner.pool.addLongInfo(value)
    }

    fun setDouble(value: Double) {
        poolIndex = owner.pool.addDoubleInfo(value)
    }

    fun setFloat(value: Float) {
        poolIndex = owner.pool.addFloatInfo(value)
    }
}
