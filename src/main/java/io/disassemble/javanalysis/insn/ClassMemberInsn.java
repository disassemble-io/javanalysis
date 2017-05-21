package io.disassemble.javanalysis.insn;

import io.disassemble.javanalysis.CtMethodNode;

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
public class ClassMemberInsn extends CtInsn {

    protected String parent, name, desc;

    public ClassMemberInsn(CtMethodNode owner, int index, int opcode, String parent, String name, String desc) {
        super(owner, index, opcode);
        this.parent = parent;
        this.name = name;
        this.desc = desc;
    }

    public String parent() {
        return parent;
    }

    public String name() {
        return name;
    }

    public String desc() {
        return desc;
    }

    public String key() {
        return parent + "." + name + desc;
    }
}
