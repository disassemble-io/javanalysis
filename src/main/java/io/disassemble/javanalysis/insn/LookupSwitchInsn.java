package io.disassemble.javanalysis.insn;

import io.disassemble.javanalysis.CtMethodNode;

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
public class LookupSwitchInsn extends CtInsn {

    protected int defaultIndex;
    protected int[] keys, indices;

    public LookupSwitchInsn(CtMethodNode owner, int index, int opcode, int defaultIndex, int[] keys, int[] indices) {
        super(owner, index, opcode);
        this.defaultIndex = defaultIndex;
        this.keys = keys;
        this.indices = indices;
    }

    public int defaultIndex() {
        return defaultIndex;
    }

    public int size() {
        return keys.length;
    }

    public int[] keys() {
        return keys;
    }

    public int[] indices() {
        return indices;
    }
}
