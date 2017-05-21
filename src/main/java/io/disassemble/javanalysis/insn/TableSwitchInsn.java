package io.disassemble.javanalysis.insn;

import io.disassemble.javanalysis.CtMethodNode;

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
public class TableSwitchInsn extends CtInsn {

    protected int low, high, defaultIndex;
    protected int[] indices;

    public TableSwitchInsn(CtMethodNode owner, int index, int opcode, int low, int high, int defaultIndex, int[] indices) {
        super(owner, index, opcode);
        this.low = low;
        this.high = high;
        this.defaultIndex = defaultIndex;
        this.indices = indices;
    }

    public int low() {
        return low;
    }

    public int high() {
        return high;
    }

    public int defaultIndex() {
        return defaultIndex;
    }

    public int[] indices() {
        return indices;
    }
}
