package io.disassemble.javanalysis.insn;

import io.disassemble.javanalysis.CtMethodNode;
import io.disassemble.javanalysis.insn.CtInsn;

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
public class TypeInsn extends CtInsn {

    protected String type;

    public TypeInsn(CtMethodNode owner, int index, int opcode, String type) {
        super(owner, index, opcode);
        this.type = type;
    }

    public String type() {
        return type;
    }
}
