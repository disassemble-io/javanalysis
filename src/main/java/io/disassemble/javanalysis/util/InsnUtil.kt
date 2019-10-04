package io.disassemble.javanalysis.util

import io.disassemble.javanalysis.insn.*
import javassist.bytecode.Opcode.*

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
object InsnUtil {

    fun isLdcInsn(opcode: Int): Boolean {
        return opcode == LDC || opcode == LDC_W || opcode == LDC2_W
    }

    fun isIntInsn(opcode: Int): Boolean {
        return opcode == BIPUSH || opcode == SIPUSH || opcode == NEWARRAY
    }

    fun isVarInsn(opcode: Int): Boolean {
        return opcode in ILOAD..ALOAD_3 || opcode in ISTORE..ASTORE_3 || opcode == RET
    }

    fun isJumpInsn(opcode: Int): Boolean {
        return opcode in IFEQ..JSR || opcode in IFNULL..JSR_W
    }

    fun isIncrementInsn(opcode: Int): Boolean {
        return opcode == IINC
    }

    fun isFieldInsn(opcode: Int): Boolean {
        return opcode in GETSTATIC..PUTFIELD
    }

    fun isMethodInsn(opcode: Int): Boolean {
        return opcode in INVOKEVIRTUAL..INVOKEINTERFACE
    }

    fun isInvokeDynamicInsn(opcode: Int): Boolean {
        return opcode == INVOKEDYNAMIC
    }

    fun isTypeInsn(opcode: Int): Boolean {
        return opcode == ANEWARRAY || opcode == CHECKCAST || opcode == MULTIANEWARRAY
    }

    fun isTableSwitchInsn(opcode: Int): Boolean {
        return opcode == TABLESWITCH
    }

    fun isLookupSwitchInsn(opcode: Int): Boolean {
        return opcode == LOOKUPSWITCH
    }

    fun isUnderscoreInsn(opcode: Int): Boolean {
        return opcode in ILOAD_0..ALOAD_3 || opcode in ISTORE_0..ASTORE_3 ||
                opcode in ICONST_M1..DCONST_1
    }

    fun underVal(opcode: Int): Int {
        when (opcode) {
            ILOAD_0, LLOAD_0, FLOAD_0, DLOAD_0, ALOAD_0, ISTORE_0, LSTORE_0, FSTORE_0, DSTORE_0, ASTORE_0, ICONST_0, LCONST_0, FCONST_0, DCONST_0 -> {
                return 0
            }
            ILOAD_1, LLOAD_1, FLOAD_1, DLOAD_1, ALOAD_1, ISTORE_1, LSTORE_1, FSTORE_1, DSTORE_1, ASTORE_1, ICONST_1, LCONST_1, FCONST_1, DCONST_1 -> {
                return 1
            }
            ILOAD_2, LLOAD_2, FLOAD_2, DLOAD_2, ALOAD_2, ISTORE_2, LSTORE_2, FSTORE_2, DSTORE_2, ASTORE_2, ICONST_2, FCONST_2 -> {
                return 2
            }
            ILOAD_3, LLOAD_3, FLOAD_3, DLOAD_3, ALOAD_3, ISTORE_3, LSTORE_3, FSTORE_3, DSTORE_3, ASTORE_3, ICONST_3 -> {
                return 3
            }
            ICONST_4 -> {
                return 4
            }
            ICONST_5 -> {
                return 5
            }
            else -> {
                return -1
            }
        }
    }

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
