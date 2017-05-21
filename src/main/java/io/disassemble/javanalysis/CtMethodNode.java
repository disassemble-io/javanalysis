package io.disassemble.javanalysis;

import io.disassemble.javanalysis.flow.ControlFlowGraph;
import io.disassemble.javanalysis.flow.FlowBlock;
import io.disassemble.javanalysis.insn.CtInsn;
import io.disassemble.javanalysis.util.CodeParser;
import javassist.CtMethod;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.analysis.ControlFlow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tyler Sedlar
 * @since 5/19/2017
 */
public class CtMethodNode {

    private final CtMethod source;
    private final CtClassNode owner;
    private List<CtInsn> instructions;

    private final Map<Integer, Integer> indices = new HashMap<>();

    public CtMethodNode(CtMethod source, CtClassNode owner) {
        this.source = source;
        this.owner = owner;
    }

    public CtMethod source() {
        return source;
    }

    public CtClassNode owner() {
        return owner;
    }

    public String name() {
        return source.getName();
    }

    public String desc() {
        return source.getSignature();
    }

    public String key() {
        return owner.name() + "." + name() + desc();
    }

    public MethodInfo info() {
        return source.getMethodInfo();
    }

    public CodeAttribute code() {
        return info().getCodeAttribute();
    }

    public ConstPool pool() {
        return info().getConstPool();
    }

    public List<CtInsn> instructions() {
        if (instructions != null) {
            return instructions;
        }
        return (instructions = CodeParser.parse(this));
    }

    public int line() {
        return instructions().get(0).line() - 1;
    }

    public void index(int insnIndex, int position) {
        indices.put(insnIndex, position);
    }

    public int indexOf(CtInsn insn) {
        return normalizeIndex(insn.index());
    }

    public int normalizeIndex(int index) {
        return (indices.containsKey(index) ? indices.get(index) : indices.get(index - 1));
    }

    public boolean hasIndex(int index) {
        return indices.containsKey(index);
    }

    public ControlFlowGraph cfg() {
        try {
            ControlFlow flow = new ControlFlow(owner.source(), info());
            List<FlowBlock> blocks = new ArrayList<>();
            for (ControlFlow.Block bb : flow.basicBlocks()) {
                blocks.add(new FlowBlock(this, bb));
            }
            ControlFlowGraph cfg = new ControlFlowGraph(flow, blocks);
            blocks.forEach(block -> block.init(cfg));
            return cfg;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Invalid bytecode for cfg in " + source.getLongName());
        }
    }
}
