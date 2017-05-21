package io.disassemble.javanalysis.insn;

import io.disassemble.javanalysis.CtMethodNode;

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
public class JumpInsn extends CtInsn {

    protected int target;

    public JumpInsn(CtMethodNode owner, int index, int opcode, int target) {
        super(owner, index, opcode);
        this.target = target;
    }

    public int target() {
        return target;
    }
}
