package io.disassemble.javanalysis.insn;

import io.disassemble.javanalysis.CtMethodNode;

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
public class MultiANewArrayInsn extends TypeInsn {

    protected int dimensions;

    public MultiANewArrayInsn(CtMethodNode owner, int index, int opcode, String type, int dimensions) {
        super(owner, index, opcode, type);
        this.dimensions = dimensions;
    }

    public int dimensions() {
        return dimensions;
    }
}
