package io.disassemble.javanalysis.insn;

import io.disassemble.javanalysis.CtMethodNode;

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
public class FieldInsn extends ClassMemberInsn {

    public FieldInsn(CtMethodNode owner, int index, int opcode, String parent, String name, String desc) {
        super(owner, index, opcode, parent, name, desc);
    }

    @Override
    public String key() {
        return parent + "." + name;
    }
}
