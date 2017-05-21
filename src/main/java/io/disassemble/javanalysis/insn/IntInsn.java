package io.disassemble.javanalysis.insn;

import io.disassemble.javanalysis.CtMethodNode;

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
public class IntInsn extends CtInsn {

    protected int operand = -1;

    public IntInsn(CtMethodNode owner, int index, int opcode, int operand) {
        super(owner, index, opcode);
        this.operand = operand;
    }

    public int operand() {
        return operand;
    }
}
