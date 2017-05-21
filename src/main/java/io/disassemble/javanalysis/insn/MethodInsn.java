package io.disassemble.javanalysis.insn;

import io.disassemble.javanalysis.CtMethodNode;

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
public class MethodInsn extends ClassMemberInsn {

    public MethodInsn(CtMethodNode owner, int index, int opcode, String parent, String name, String desc) {
        super(owner, index, opcode, parent, name, desc);
    }
}
