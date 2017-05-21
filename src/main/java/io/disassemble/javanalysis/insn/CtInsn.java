package io.disassemble.javanalysis.insn;

import io.disassemble.javanalysis.CtMethodNode;
import javassist.bytecode.Mnemonic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
public class CtInsn {

    protected final CtMethodNode owner;
    protected int index, opcode;

    public final AtomicReference<CtInsn> previous = new AtomicReference<>();
    public final AtomicReference<CtInsn> next = new AtomicReference<>();

    public CtInsn(CtMethodNode owner, int index, int opcode) {
        this.owner = owner;
        this.index = index;
        this.opcode = opcode;
    }

    public CtMethodNode owner() {
        return owner;
    }

    public int index() {
        return index;
    }

    public int opcode() {
        return opcode;
    }

    public String opname() {
        return Mnemonic.OPCODE[opcode];
    }

    public int line() {
        return owner.info().getLineNumber(index);
    }

    public int relativeLine() {
        return (line() - owner.line());
    }

    public int position() {
        return owner.indexOf(this);
    }

    public boolean hasPrevious() {
        return previous.get() != null;
    }

    public boolean hasNext() {
        return next.get() != null;
    }

    @Override
    public String toString() {
        return opname();
    }
}
