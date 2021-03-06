package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import io.disassemble.javanalysis.pool
import javassist.CtMethod
import javassist.bytecode.Opcode

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */

/**
 * A class representing a typed instruction.
 *
 * @param owner The [CtMethod] that this instruction is a part of.
 * @param index The index of this instruction in [CtMethod].
 */
open class TypeInsn(
        owner: CtMethod,
        index: Int
) : CtInsn(owner, index) {

    private var poolIndex = if (opcode == Opcode.NEWARRAY) {
        owner.code.iterator().byteAt(index + 1)
    } else {
        owner.code.iterator().u16bitAt(index + 1)
    }

    /**
     * The type of this [TypeInsn].
     */
    var type: String
        get() {
            return if (opcode == Opcode.NEWARRAY) {
                idx2type(poolIndex)
            } else {
                owner.pool.getClassInfo(poolIndex)
            }
        }
        set(value) {
            if (opcode == Opcode.NEWARRAY) {
                poolIndex = type2idx(value)
                owner.code.iterator().writeByte(poolIndex, index + 1)
            } else {
                poolIndex = owner.pool.addClassInfo(value)
                owner.code.iterator().write16bit(poolIndex, index + 1)
            }
        }

    /**
     * Converts a type tag to its associated string.
     *
     * @param type The type tag to convert.
     *
     * @return The [String] representation of the given tag.
     */
    private fun idx2type(type: Int): String {
        return when (type) {
            Opcode.T_BOOLEAN -> "boolean"
            Opcode.T_CHAR -> "char"
            Opcode.T_BYTE -> "byte"
            Opcode.T_SHORT -> "short"
            Opcode.T_INT -> "int"
            Opcode.T_LONG -> "long"
            Opcode.T_FLOAT -> "float"
            Opcode.T_DOUBLE -> "double"
            else -> throw RuntimeException("Invalid array type")
        }
    }

    /**
     * Converts [String] type tag to its opcode counterpart.
     *
     * @param type The [String] type tag to convert.
     *
     * @return The opcode representation of the given tag.
     */
    private fun type2idx(type: String): Int {
        return when (type) {
            "boolean" -> Opcode.T_BOOLEAN
            "char" -> Opcode.T_CHAR
            "byte" -> Opcode.T_BYTE
            "short" -> Opcode.T_SHORT
            "int" -> Opcode.T_INT
            "long" -> Opcode.T_LONG
            "float" -> Opcode.T_FLOAT
            "double" -> Opcode.T_DOUBLE
            else -> throw RuntimeException("Invalid array type")
        }
    }
}