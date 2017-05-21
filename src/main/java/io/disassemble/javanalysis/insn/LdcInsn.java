package io.disassemble.javanalysis.insn;

import io.disassemble.javanalysis.CtMethodNode;

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
public class LdcInsn extends CtInsn {

    protected int poolIndex;

    public LdcInsn(CtMethodNode owner, int index, int opcode, int poolIndex) {
        super(owner, index, opcode);
        this.poolIndex = poolIndex;
    }

    public int poolIndex() {
        return poolIndex;
    }

    public int tag() {
        return owner.info().getConstPool().getTag(poolIndex());
    }

    public Object cst() {
        return owner.pool().getLdcValue(poolIndex());
    }
}
