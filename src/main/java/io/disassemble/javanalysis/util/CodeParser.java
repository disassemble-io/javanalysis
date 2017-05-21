package io.disassemble.javanalysis.util;

import io.disassemble.javanalysis.CtMethodNode;
import io.disassemble.javanalysis.insn.*;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;

import java.util.ArrayList;
import java.util.List;

import static javassist.bytecode.Opcode.*;

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
public class CodeParser {

    public static List<CtInsn> parse(CtMethodNode method) {
        try {
            List<CtInsn> instructions = new ArrayList<>();
            ConstPool pool = method.pool();
            CodeAttribute attr = method.code();
            CodeIterator itr = attr.iterator();
            CtInsn previous = null;
            while (itr.hasNext()) {
                int index = itr.next();
                int opcode = itr.byteAt(index);
                int poolIndex = (index + 1);
                CtInsn insn;
                if (InsnUtil.isLdcInsn(opcode)) {
                    boolean wide = (opcode == LDC_W || opcode == LDC2_W);
                    poolIndex = (wide ? itr.u16bitAt(poolIndex) : itr.byteAt(poolIndex));
                    insn = new LdcInsn(method, index, opcode, poolIndex);
                } else if (InsnUtil.isIntInsn(opcode)) {
                    boolean s32 = (opcode == SIPUSH);
                    int operand = (s32 ? itr.s32bitAt(poolIndex) : itr.byteAt(poolIndex));
                    insn = new IntInsn(method, index, opcode, operand);
                } else if (InsnUtil.isVarInsn(opcode)) {
                    int var = (InsnUtil.isUnderscoreInsn(opcode) ? InsnUtil.underVal(opcode) : itr.byteAt(poolIndex));
                    insn = new VarInsn(method, index, opcode, var);
                } else if (InsnUtil.isJumpInsn(opcode)) {
                    boolean wide = (opcode == GOTO_W || opcode == JSR_W);
                    int target = ((wide ? itr.s32bitAt(poolIndex) : itr.s16bitAt(poolIndex)) + index);
                    insn = new JumpInsn(method, index, opcode, target);
                } else if (InsnUtil.isIncrementInsn(opcode)) {
                    int var = itr.byteAt(poolIndex);
                    int increment = itr.signedByteAt(poolIndex + 1);
                    insn = new IncrementInsn(method, index, opcode, var, increment);
                } else if (InsnUtil.isFieldInsn(opcode)) {
                    poolIndex = itr.u16bitAt(poolIndex);
                    String parent = pool.getFieldrefClassName(poolIndex);
                    String name = pool.getFieldrefName(poolIndex);
                    String desc = pool.getFieldrefType(poolIndex);
                    insn = new FieldInsn(method, index, opcode, parent, name, desc);
                } else if (InsnUtil.isMethodInsn(opcode)) {
                    poolIndex = itr.u16bitAt(poolIndex);
                    if (opcode == INVOKEINTERFACE) {
                        String parent = pool.getInterfaceMethodrefClassName(poolIndex);
                        String name = pool.getInterfaceMethodrefName(poolIndex);
                        String desc = pool.getInterfaceMethodrefType(poolIndex);
                        insn = new MethodInsn(method, index, opcode, parent, name, desc);
                    } else {
                        String parent = pool.getMethodrefClassName(poolIndex);
                        String name = pool.getMethodrefName(poolIndex);
                        String desc = pool.getMethodrefType(poolIndex);
                        insn = new MethodInsn(method, index, opcode, parent, name, desc);
                    }
                } else if (InsnUtil.isInvokeDynamicInsn(opcode)) {
                    poolIndex = itr.u16bitAt(poolIndex);
                    String type = pool.getInvokeDynamicType(poolIndex);
                    int nameIndex = pool.getInvokeDynamicNameAndType(poolIndex);
                    String name = pool.getUtf8Info(nameIndex); // TODO: check that this is correct.
                    int bootstrap = pool.getInvokeDynamicBootstrap(poolIndex);
                    insn = new InvokeDynamicInsn(method, index, opcode, name, type, bootstrap);
                } else if (InsnUtil.isTypeInsn(opcode)) {
                    poolIndex = itr.u16bitAt(poolIndex);
                    String type = pool.getClassInfo(poolIndex);
                    if (opcode == MULTIANEWARRAY) {
                        int dims = -1; // handle?
                        insn = new MultiANewArrayInsn(method, index, opcode, type, dims);
                    } else {
                        insn = new TypeInsn(method, index, opcode, type);
                    }
                } else if (opcode == WIDE) {
                    opcode = itr.byteAt(poolIndex);
                    index = itr.u16bitAt(poolIndex + 1);
                    insn = new CtInsn(method, index, opcode);
                } else if (InsnUtil.isTableSwitchInsn(opcode)) {
                    int key = (index & ~3) + 4;
                    int dfltIdx = (index + itr.s32bitAt(key));
                    int low = itr.s32bitAt(key += 4);
                    int high = itr.s32bitAt(key += 4);
                    int end = (high - low + 1) * 4 + (key += 4);
                    int size = low + ((end - index) / 4);
                    int[] indices = new int[size];
                    for (int idx = low; index < end; index += 4, idx++) {
                        indices[idx - low] = (index + itr.s32bitAt(key));
                    }
                    insn = new TableSwitchInsn(method, index, opcode, low, high, dfltIdx, indices);
                } else if (InsnUtil.isLookupSwitchInsn(opcode)) {
                    int key = (index & ~3) + 4;
                    int dfltIdx = (index + itr.s32bitAt(key));
                    int size = itr.s32bitAt(key += 4);
                    int[] keys = new int[size];
                    int[] indices = new int[size];
                    key += 4;
                    for (int i = 0; i < size; i++) {
                        int match = itr.s32bitAt(key);
                        int target = (index + itr.s32bitAt(key + 4));
                        keys[i] = match;
                        indices[i] = target;
                        key += 8;
                    }
                    insn = new LookupSwitchInsn(method, index, opcode, dfltIdx, keys, indices);
                } else {
                    insn = new CtInsn(method, index, opcode);
                }
                if (previous != null) {
                    previous.next.set(insn);
                    insn.previous.set(previous);
                }
                previous = insn;
                method.index(insn.index(), instructions.size());
                instructions.add(insn);
            }
            return instructions;
        } catch (BadBytecode e) {
            throw new IllegalStateException("Unable to parse instructions for " + method.source().getLongName());
        }
    }
}
