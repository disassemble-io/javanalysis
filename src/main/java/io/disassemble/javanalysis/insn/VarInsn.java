package io.disassemble.javanalysis.insn;

import io.disassemble.javanalysis.CtMethodNode;

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
public class VarInsn extends CtInsn {

    protected int var;

    public VarInsn(CtMethodNode owner, int index, int opcode, int var) {
        super(owner, index, opcode);
        this.var = var;
    }

    public int var() {
        return var;
    }
}
