package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import javassist.CtMethod
import javassist.bytecode.CodeIterator
import javassist.bytecode.Opcode

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */

/**
 * A class representing a local variable instruction.
 *
 * @param owner The [CtMethod] that this instruction is a part of.
 * @param index The index of this instruction in [CtMethod].
 */
class VarInsn(
        owner: CtMethod,
        index: Int
) : CtInsn(owner, index) {

    /**
     * The variable on the stack.
     */
    var variable: Int
        get() {
            return when (opcode) {
                Opcode.ILOAD_0, Opcode.LLOAD_0, Opcode.FLOAD_0, Opcode.DLOAD_0, Opcode.ALOAD_0,
                Opcode.ISTORE_0, Opcode.LSTORE_0, Opcode.FSTORE_0, Opcode.DSTORE_0, Opcode.ASTORE_0 -> 0
                Opcode.ILOAD_1, Opcode.LLOAD_1, Opcode.FLOAD_1, Opcode.DLOAD_1, Opcode.ALOAD_1,
                Opcode.ISTORE_1, Opcode.LSTORE_1, Opcode.FSTORE_1, Opcode.DSTORE_1, Opcode.ASTORE_1 -> 1
                Opcode.ILOAD_2, Opcode.LLOAD_2, Opcode.FLOAD_2, Opcode.DLOAD_2, Opcode.ALOAD_2,
                Opcode.ISTORE_2, Opcode.LSTORE_2, Opcode.FSTORE_2, Opcode.DSTORE_2, Opcode.ASTORE_2 -> 2
                Opcode.ILOAD_3, Opcode.LLOAD_3, Opcode.FLOAD_3, Opcode.DLOAD_3, Opcode.ALOAD_3,
                Opcode.ISTORE_3, Opcode.LSTORE_3, Opcode.FSTORE_3, Opcode.DSTORE_3, Opcode.ASTORE_3 -> 3
                else -> owner.code.iterator().byteAt(index + 1) // non-wide, between 4-255
            }
        }
        set(value) = writeVar(value)

    private fun writeVar(value: Int) {
        require(value >= 0) { "VarInsn#variable must be greater than 0" }

        if (isNotSmall()) {
            if (value > 255) {
                require(previous != null && previous!!.opcode == Opcode.WIDE) {
                    "Using a value > 255 requires a WIDE instruction before LOAD/STORE"
                }

                owner.code.iterator().write16bit(value, index + 1)
            } else {
                owner.code.iterator().writeByte(value, index + 1)
            }
        } else {
            require(value in 0..3) { "LOAD_/STORE_ can only use values 0-3" }
            changeVal(this, value)
        }
    }

    private fun isNotSmall(): Boolean {
        return opcode == Opcode.ILOAD || opcode == Opcode.LLOAD || opcode == Opcode.FLOAD ||
                opcode == Opcode.DLOAD || opcode == Opcode.ALOAD || opcode == Opcode.ISTORE ||
                opcode == Opcode.LSTORE || opcode == Opcode.FSTORE || opcode == Opcode.DSTORE ||
                opcode == Opcode.ASTORE
    }

    companion object {

        /**
         * Normalizes this instruction by changing iload_1 to iload, etc.
         *
         * @param iter The [CodeIterator] to change [CtInsn] within.
         * @param index The index of the [CtInsn] to change.
         * @param op The opcode to be normalized.
         */
        fun normalize(iter: CodeIterator, index: Int, op: Int) {
            when (op) {
                Opcode.ILOAD_0, Opcode.ILOAD_1, Opcode.ILOAD_2, Opcode.ILOAD_3 -> {
                    iter.writeByte(Opcode.ILOAD, index)
                }
                Opcode.LLOAD_0, Opcode.LLOAD_1, Opcode.LLOAD_2, Opcode.LLOAD_3 -> {
                    iter.writeByte(Opcode.LLOAD, index)
                }
                Opcode.FLOAD_0, Opcode.FLOAD_1, Opcode.FLOAD_2, Opcode.FLOAD_3 -> {
                    iter.writeByte(Opcode.FLOAD, index)
                }
                Opcode.DLOAD_0, Opcode.DLOAD_1, Opcode.DLOAD_2, Opcode.DLOAD_3 -> {
                    iter.writeByte(Opcode.DLOAD, index)
                }
                Opcode.ALOAD_0, Opcode.ALOAD_1, Opcode.ALOAD_2, Opcode.ALOAD_3 -> {
                    iter.writeByte(Opcode.ALOAD, index)
                }
                Opcode.ISTORE_0, Opcode.ISTORE_1, Opcode.ISTORE_2, Opcode.ISTORE_3 -> {
                    iter.writeByte(Opcode.ISTORE, index)
                }
                Opcode.LSTORE_0, Opcode.LSTORE_1, Opcode.LSTORE_2, Opcode.LSTORE_3 -> {
                    iter.writeByte(Opcode.LSTORE, index)
                }
                Opcode.FSTORE_0, Opcode.FSTORE_1, Opcode.FSTORE_2, Opcode.FSTORE_3 -> {
                    iter.writeByte(Opcode.FSTORE, index)
                }
                Opcode.DSTORE_0, Opcode.DSTORE_1, Opcode.DSTORE_2, Opcode.DSTORE_3 -> {
                    iter.writeByte(Opcode.DSTORE, index)
                }
                Opcode.ASTORE_0, Opcode.ASTORE_1, Opcode.ASTORE_2, Opcode.ASTORE_3 -> {
                    iter.writeByte(Opcode.ASTORE, index)
                }
            }

            val value: Byte = when (op) {
                Opcode.ILOAD_0, Opcode.LLOAD_0, Opcode.FLOAD_0, Opcode.DLOAD_0, Opcode.ALOAD_0,
                Opcode.ISTORE_0, Opcode.LSTORE_0, Opcode.FSTORE_0, Opcode.DSTORE_0, Opcode.ASTORE_0 -> 0
                Opcode.ILOAD_1, Opcode.LLOAD_1, Opcode.FLOAD_1, Opcode.DLOAD_1, Opcode.ALOAD_1,
                Opcode.ISTORE_1, Opcode.LSTORE_1, Opcode.FSTORE_1, Opcode.DSTORE_1, Opcode.ASTORE_1 -> 1
                Opcode.ILOAD_2, Opcode.LLOAD_2, Opcode.FLOAD_2, Opcode.DLOAD_2, Opcode.ALOAD_2,
                Opcode.ISTORE_2, Opcode.LSTORE_2, Opcode.FSTORE_2, Opcode.DSTORE_2, Opcode.ASTORE_2 -> 2
                Opcode.ILOAD_3, Opcode.LLOAD_3, Opcode.FLOAD_3, Opcode.DLOAD_3, Opcode.ALOAD_3,
                Opcode.ISTORE_3, Opcode.LSTORE_3, Opcode.FSTORE_3, Opcode.DSTORE_3, Opcode.ASTORE_3 -> 3
                else -> return
            }

            iter.insert(index + 1, byteArrayOf(value))
        }

        /**
         * Changes the variable value within its underscored class. (iload_0->iload_1, iload_0->iload_2, etc.)
         *
         * @param insn The [VarInsn] to be changed.
         * @param value The value to be changed to.
         */
        private fun changeVal(insn: VarInsn, value: Int) {
            when (insn.opcode) {
                Opcode.ILOAD_0, Opcode.ILOAD_1, Opcode.ILOAD_2, Opcode.ILOAD_3 -> {
                    insn.opcode = Opcode.ILOAD_0 + value
                }
                Opcode.LLOAD_0, Opcode.LLOAD_1, Opcode.LLOAD_2, Opcode.LLOAD_3 -> {
                    insn.opcode = Opcode.LLOAD_0 + value
                }
                Opcode.FLOAD_0, Opcode.FLOAD_1, Opcode.FLOAD_2, Opcode.FLOAD_3 -> {
                    insn.opcode = Opcode.FLOAD_0 + value
                }
                Opcode.DLOAD_0, Opcode.DLOAD_1, Opcode.DLOAD_2, Opcode.DLOAD_3 -> {
                    insn.opcode = Opcode.DLOAD_0 + value
                }
                Opcode.ALOAD_0, Opcode.ALOAD_1, Opcode.ALOAD_2, Opcode.ALOAD_3 -> {
                    insn.opcode = Opcode.ALOAD_0 + value
                }
                Opcode.ISTORE_0, Opcode.ISTORE_1, Opcode.ISTORE_2, Opcode.ISTORE_3 -> {
                    insn.opcode = Opcode.ISTORE_0 + value
                }
                Opcode.LSTORE_0, Opcode.LSTORE_1, Opcode.LSTORE_2, Opcode.LSTORE_3 -> {
                    insn.opcode = Opcode.LSTORE_0 + value
                }
                Opcode.FSTORE_0, Opcode.FSTORE_1, Opcode.FSTORE_2, Opcode.FSTORE_3 -> {
                    insn.opcode = Opcode.FSTORE_0 + value
                }
                Opcode.DSTORE_0, Opcode.DSTORE_1, Opcode.DSTORE_2, Opcode.DSTORE_3 -> {
                    insn.opcode = Opcode.DSTORE_0 + value
                }
                Opcode.ASTORE_0, Opcode.ASTORE_1, Opcode.ASTORE_2, Opcode.ASTORE_3 -> {
                    insn.opcode = Opcode.ASTORE_0 + value
                }
            }
        }
    }
}