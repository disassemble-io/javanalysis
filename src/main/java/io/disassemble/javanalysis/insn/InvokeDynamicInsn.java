package io.disassemble.javanalysis.insn;

import io.disassemble.javanalysis.CtMethodNode;

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
public class InvokeDynamicInsn extends CtInsn {

    protected String name, type;
    protected int bootstrap;

    public InvokeDynamicInsn(CtMethodNode owner, int index, int opcode, String name, String type, int bootstrap) {
        super(owner, index, opcode);
        this.name = name;
        this.type = type;
        this.bootstrap = bootstrap;
    }

    public String name() {
        return name;
    }

    public String type() {
        return type;
    }

    public int bootstrap() {
        return bootstrap;
    }
}
