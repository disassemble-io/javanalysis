package io.disassemble.javanalysis.util;

import io.disassemble.javanalysis.insn.*;

import java.util.Arrays;

import static javassist.bytecode.Opcode.*;

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
public class InsnUtil {

    public static boolean isLdcInsn(int opcode) {
        return opcode == LDC || opcode == LDC_W || opcode == LDC2_W;
    }

    public static boolean isIntInsn(int opcode) {
        return opcode == BIPUSH || opcode == SIPUSH || opcode == NEWARRAY;
    }

    public static boolean isVarInsn(int opcode) {
        return (opcode >= ILOAD && opcode <= ALOAD_3) || (opcode >= ISTORE && opcode <= ASTORE_3) || opcode == RET;
    }

    public static boolean isJumpInsn(int opcode) {
        return (opcode >= IFEQ && opcode <= JSR) || (opcode >= IFNULL && opcode <= JSR_W);
    }

    public static boolean isIncrementInsn(int opcode) {
        return opcode == IINC;
    }

    public static boolean isFieldInsn(int opcode) {
        return opcode >= GETSTATIC && opcode <= PUTFIELD;
    }

    public static boolean isMethodInsn(int opcode) {
        return opcode >= INVOKEVIRTUAL && opcode <= INVOKEINTERFACE;
    }

    public static boolean isInvokeDynamicInsn(int opcode) {
        return opcode == INVOKEDYNAMIC;
    }

    public static boolean isTypeInsn(int opcode) {
        return opcode == ANEWARRAY || opcode == CHECKCAST || opcode == MULTIANEWARRAY;
    }

    public static boolean isTableSwitchInsn(int opcode) {
        return opcode == TABLESWITCH;
    }

    public static boolean isLookupSwitchInsn(int opcode) {
        return opcode == LOOKUPSWITCH;
    }

    public static boolean isUnderscoreInsn(int opcode) {
        return (opcode >= ILOAD_0 && opcode <= ALOAD_3) || (opcode >= ISTORE_0 && opcode <= ASTORE_3) ||
                (opcode >= ICONST_M1 && opcode <= DCONST_1);
    }

    public static int underVal(int opcode) {
        switch(opcode) {
            case ILOAD_0:
            case LLOAD_0:
            case FLOAD_0:
            case DLOAD_0:
            case ALOAD_0:
            case ISTORE_0:
            case LSTORE_0:
            case FSTORE_0:
            case DSTORE_0:
            case ASTORE_0:
            case ICONST_0:
            case LCONST_0:
            case FCONST_0:
            case DCONST_0: {
                return 0;
            }
            case ILOAD_1:
            case LLOAD_1:
            case FLOAD_1:
            case DLOAD_1:
            case ALOAD_1:
            case ISTORE_1:
            case LSTORE_1:
            case FSTORE_1:
            case DSTORE_1:
            case ASTORE_1:
            case ICONST_1:
            case LCONST_1:
            case FCONST_1:
            case DCONST_1: {
                return 1;
            }
            case ILOAD_2:
            case LLOAD_2:
            case FLOAD_2:
            case DLOAD_2:
            case ALOAD_2:
            case ISTORE_2:
            case LSTORE_2:
            case FSTORE_2:
            case DSTORE_2:
            case ASTORE_2:
            case ICONST_2:
            case FCONST_2: {
                return 2;
            }
            case ILOAD_3:
            case LLOAD_3:
            case FLOAD_3:
            case DLOAD_3:
            case ALOAD_3:
            case ISTORE_3:
            case LSTORE_3:
            case FSTORE_3:
            case DSTORE_3:
            case ASTORE_3:
            case ICONST_3: {
                return 3;
            }
            case ICONST_4: {
                return 4;
            }
            case ICONST_5: {
                return 5;
            }
            default: {
                return -1;
            }
        }
    }

    public static String stringify(CtInsn insn) {
        String label = "";
        Object data = "";
        if (insn instanceof LdcInsn) {
            label = "cst";
            data = ((LdcInsn) insn).cst();
        } else if (insn instanceof IntInsn) {
            label = "operand";
            data = ((IntInsn) insn).operand();
        } else if (insn instanceof VarInsn) {
            label = "var";
            data = ((VarInsn) insn).var();
        } else if (insn instanceof JumpInsn) {
            label = "target";
            data = ((JumpInsn) insn).target();
        } else if (insn instanceof ClassMemberInsn) {
            label = (insn instanceof FieldInsn ? "field" : "method");
            data = ((ClassMemberInsn) insn).key();
        } else if (insn instanceof TypeInsn) {
            label = "type";
            data = ((TypeInsn) insn).type();
        } else if (insn instanceof TableSwitchInsn) {
            label = "data";
            TableSwitchInsn table = (TableSwitchInsn) insn;
            data = ("low = " + table.low() + ", high = " + table.high() + ", defaultIndex = " +
                    table.defaultIndex() + ", indices = " + Arrays.toString(table.indices()));
        } else if (insn instanceof LookupSwitchInsn) {
            label = "data";
            LookupSwitchInsn table = (LookupSwitchInsn) insn;
            data = ("defaultIndex = " + table.defaultIndex() + ", keys = " + Arrays.toString(table.keys()) +
                    ", indices = " + Arrays.toString(table.indices()));
        }
        String output = insn.index() + ": " + insn.opname();
        if (!label.isEmpty()) {
            output += (" (" + label + ": " + data + ")");
        }
        return output;
    }
}
