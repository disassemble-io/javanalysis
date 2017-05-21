package io.disassemble.javanalysis.insn;

import io.disassemble.javanalysis.CtMethodNode;

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
public class IncrementInsn extends CtInsn {

    protected int var, increment;

    public IncrementInsn(CtMethodNode owner, int index, int opcode, int var, int increment) {
        super(owner, index, opcode);
        this.var = var;
        this.increment = increment;
    }

    public int var() {
        return var;
    }

    public int increment() {
        return increment;
    }
}
