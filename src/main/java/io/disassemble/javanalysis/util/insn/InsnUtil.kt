package io.disassemble.javanalysis.util.insn

import io.disassemble.javanalysis.insn.*
import javassist.bytecode.Opcode

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
object InsnUtil {

    /**
     * Checks if the given opcode is an [LdcInsn].
     *
     * @param opcode The opcode to check against.
     *
     * @return true if the given opcode is an [LdcInsn], otherwise false.
     */
    fun isLdcInsn(opcode: Int): Boolean {
        return opcode == Opcode.LDC || opcode == Opcode.LDC_W || opcode == Opcode.LDC2_W
    }

    /**
     * Checks if the given opcode is an [IntInsn].
     *
     * @param opcode The opcode to check against.
     *
     * @return true if the given opcode is an [IntInsn], otherwise false.
     */
    fun isIntInsn(opcode: Int): Boolean {
        return opcode == Opcode.BIPUSH || opcode == Opcode.SIPUSH || opcode == Opcode.NEWARRAY
    }

    /**
     * Checks if the given opcode is a [VarInsn].
     *
     * @param opcode The opcode to check against.
     *
     * @return true if the given opcode is a [VarInsn], otherwise false.
     */
    fun isVarInsn(opcode: Int): Boolean {
        return opcode in Opcode.ILOAD..Opcode.ALOAD_3 || opcode in Opcode.ISTORE..Opcode.ASTORE_3 || opcode == Opcode.RET
    }

    /**
     * Checks if the given opcode is a [JumpInsn].
     *
     * @param opcode The opcode to check against.
     *
     * @return true if the given opcode is a [JumpInsn], otherwise false.
     */
    fun isJumpInsn(opcode: Int): Boolean {
        return opcode in Opcode.IFEQ..Opcode.JSR || opcode in Opcode.IFNULL..Opcode.JSR_W
    }

    /**
     * Checks if the given opcode is a [IncrementInsn].
     *
     * @param opcode The opcode to check against.
     *
     * @return true if the given opcode is a [IncrementInsn], otherwise false.
     */
    fun isIncrementInsn(opcode: Int): Boolean {
        return opcode == Opcode.IINC
    }

    /**
     * Checks if the given opcode is a [FieldInsn].
     *
     * @param opcode The opcode to check against.
     *
     * @return true if the given opcode is a [FieldInsn], otherwise false.
     */
    fun isFieldInsn(opcode: Int): Boolean {
        return opcode in Opcode.GETSTATIC..Opcode.PUTFIELD
    }

    /**
     * Checks if the given opcode is a [MethodInsn].
     *
     * @param opcode The opcode to check against.
     *
     * @return true if the given opcode is a [MethodInsn], otherwise false.
     */
    fun isMethodInsn(opcode: Int): Boolean {
        return opcode in Opcode.INVOKEVIRTUAL..Opcode.INVOKEINTERFACE
    }

    /**
     * Checks if the given opcode is an [InvokeDynamicInsn].
     *
     * @param opcode The opcode to check against.
     *
     * @return true if the given opcode is an [InvokeDynamicInsn], otherwise false.
     */
    fun isInvokeDynamicInsn(opcode: Int): Boolean {
        return opcode == Opcode.INVOKEDYNAMIC
    }

    /**
     * Checks if the given opcode is a [TypeInsn].
     *
     * @param opcode The opcode to check against.
     *
     * @return true if the given opcode is a [TypeInsn], otherwise false.
     */
    fun isTypeInsn(opcode: Int): Boolean {
        return opcode == Opcode.ANEWARRAY || opcode == Opcode.CHECKCAST || opcode == Opcode.MULTIANEWARRAY
    }

    /**
     * Checks if the given opcode is a [TableSwitchInsn].
     *
     * @param opcode The opcode to check against.
     *
     * @return true if the given opcode is a [TableSwitchInsn], otherwise false.
     */
    fun isTableSwitchInsn(opcode: Int): Boolean {
        return opcode == Opcode.TABLESWITCH
    }

    /**
     * Checks if the given opcode is a [LookupSwitchInsn].
     *
     * @param opcode The opcode to check against.
     *
     * @return true if the given opcode is a [LookupSwitchInsn], otherwise false.
     */
    fun isLookupSwitchInsn(opcode: Int): Boolean {
        return opcode == Opcode.LOOKUPSWITCH
    }

    /**
     * Checks if the given opcode is an opcode containing an underscore (iload_0, iload_1, etc.)
     *
     * @param opcode The opcode to check against.
     *
     * @return true if the given opcode is an ontaining an underscore (iload_0, iload_1, etc.)
     */
    fun isUnderscoreInsn(opcode: Int): Boolean {
        return opcode in Opcode.ILOAD_0..Opcode.ALOAD_3 || opcode in Opcode.ISTORE_0..Opcode.ASTORE_3 ||
                opcode in Opcode.ICONST_M1..Opcode.DCONST_1
    }

    /**
     * Gets the value of the underscore variable. (iload_0 = 0, iload_1 = 1, etc.)
     *
     * @param opcode The opcode to get the underscore value from.
     *
     * @return The value of the underscore variable. (iload_0 = 0, iload_1 = 1, etc.)
     */
    fun underVal(opcode: Int): Int {
        when (opcode) {
            Opcode.ILOAD_0, Opcode.LLOAD_0, Opcode.FLOAD_0, Opcode.DLOAD_0, Opcode.ALOAD_0, Opcode.ISTORE_0, Opcode.LSTORE_0,
            Opcode.FSTORE_0, Opcode.DSTORE_0, Opcode.ASTORE_0, Opcode.ICONST_0, Opcode.LCONST_0, Opcode.FCONST_0, Opcode.DCONST_0 -> {
                return 0
            }
            Opcode.ILOAD_1, Opcode.LLOAD_1, Opcode.FLOAD_1, Opcode.DLOAD_1, Opcode.ALOAD_1, Opcode.ISTORE_1, Opcode.LSTORE_1,
            Opcode.FSTORE_1, Opcode.DSTORE_1, Opcode.ASTORE_1, Opcode.ICONST_1, Opcode.LCONST_1, Opcode.FCONST_1, Opcode.DCONST_1 -> {
                return 1
            }
            Opcode.ILOAD_2, Opcode.LLOAD_2, Opcode.FLOAD_2, Opcode.DLOAD_2, Opcode.ALOAD_2, Opcode.ISTORE_2, Opcode.LSTORE_2,
            Opcode.FSTORE_2, Opcode.DSTORE_2, Opcode.ASTORE_2, Opcode.ICONST_2, Opcode.FCONST_2 -> {
                return 2
            }
            Opcode.ILOAD_3, Opcode.LLOAD_3, Opcode.FLOAD_3, Opcode.DLOAD_3, Opcode.ALOAD_3, Opcode.ISTORE_3, Opcode.LSTORE_3,
            Opcode.FSTORE_3, Opcode.DSTORE_3, Opcode.ASTORE_3, Opcode.ICONST_3 -> {
                return 3
            }
            Opcode.ICONST_4 -> {
                return 4
            }
            Opcode.ICONST_5 -> {
                return 5
            }
            else -> {
                return -1
            }
        }
    }

    /**
     * Gets the string representation of the given [CtInsn].
     *
     * @param insn The [CtInsn] to get a visual representation for.
     *
     * @return The string representation of the given [CtInsn].
     */
    fun stringify(insn: CtInsn): String {
        var label = ""
        var data: Any = ""
        when (insn) {
            is LdcInsn -> {
                label = "cst"
                data = insn.cst
            }
            is IntInsn -> {
                label = "operand"
                data = insn.operand
            }
            is VarInsn -> {
                label = "var"
                data = insn.variable
            }
            is JumpInsn -> {
                label = "target"
                data = insn.target
            }
            is ClassMemberInsn -> {
                label = if (insn is FieldInsn) "field" else "method"
                data = insn.key
            }
            is TypeInsn -> {
                label = "type"
                data = insn.type
            }
            is TableSwitchInsn -> {
                label = "data"
                data = "low = " + insn.low + ", high = " + insn.high + ", defaultIndex = " +
                        insn.defaultIndex + ", indices = " + insn.indices.contentToString()
            }
            is LookupSwitchInsn -> {
                label = "data"
                data = "defaultIndex = " + insn.defaultIndex + ", keys = " + insn.keys.contentToString() +
                        ", indices = " + insn.indices.contentToString()
            }
            is IncrementInsn -> {
                label = "data"
                data = "var = " + insn.variable + ", incr = " + insn.increment
            }
        }
        var output = insn.opname
        if (label.isNotEmpty()) {
            output += " ($label: $data)"
        }
        return output
    }
}
