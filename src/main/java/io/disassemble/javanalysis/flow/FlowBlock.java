package io.disassemble.javanalysis.flow;

import io.disassemble.javanalysis.CtMethodNode;
import io.disassemble.javanalysis.insn.CtInsn;
import javassist.bytecode.analysis.ControlFlow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
public class FlowBlock {

    private List<CtInsn> instructions = new ArrayList<>();

    private final CtMethodNode method;
    private final ControlFlow.Block source;

    private FlowBlock[] exits;
    private FlowBlock[] incoming;

    public FlowBlock(CtMethodNode method, ControlFlow.Block source) {
        this.method = method;
        this.source = source;
    }

    public ControlFlow.Block source() {
        return source;
    }

    public int startIndex() {
        return source.position();
    }

    public int endIndex() {
        return (startIndex() + source.length());
    }

    public List<CtInsn> instructions() {
        return instructions;
    }

    public FlowBlock[] exits() {
        return exits;
    }

    public int[] exitIndices() {
        int[] indices = new int[exits.length];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = exits[i].startIndex();
        }
        return indices;
    }

    public FlowBlock[] incoming() {
        return incoming;
    }

    public int[] incomingIndices() {
        int[] indices = new int[incoming.length];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = incoming[i].startIndex();
        }
        return indices;
    }

    public ControlFlow.Catcher[] catchers() {
        return source.catchers();
    }

    public void init(ControlFlowGraph cfg) {
        if (exits != null) {
            return; // already initialized
        }
        constructInstructions(method.instructions());
        constructExits(cfg);
        constructIncoming(cfg);
    }

    private void constructInstructions(List<CtInsn> instructions) {
        int start = method.normalizeIndex(startIndex());
        int end = method.normalizeIndex(endIndex());
        int offset = (method.hasIndex(endIndex()) ? 0 : 1); // this occurs on the end source
        this.instructions.addAll(instructions.subList(start, end + offset));
    }

    private void constructExits(ControlFlowGraph cfg) {
        exits = new FlowBlock[source.exits()];
        for (int i = 0; i < exits.length; i++) {
            exits[i] = translate(cfg, source.exit(i));
        }
    }

    private void constructIncoming(ControlFlowGraph cfg) {
        incoming = new FlowBlock[source.incomings()];
        for (int i = 0; i < incoming.length; i++) {
            incoming[i] = translate(cfg, source.incoming(i));
        }
    }

    public static FlowBlock translate(ControlFlowGraph cfg, ControlFlow.Block block) {
        return cfg.findBlockByIndex(block.position());
    }
}
