package io.disassemble.javanalysis.flow;

import javassist.bytecode.analysis.ControlFlow;

import java.util.*;

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
public class ControlFlowGraph {

    private final ControlFlow flow;
    private final List<FlowBlock> blocks;

    public ControlFlowGraph(ControlFlow flow, List<FlowBlock> blocks) {
        this.flow = flow;
        this.blocks = blocks;
    }

    public ControlFlow flow() {
        return flow;
    }

    public List<FlowBlock> blocks() {
        return blocks;
    }

    public Collection<FlowNode> dominatorTree() {
        Map<ControlFlow.Node, FlowNode> nodes = new LinkedHashMap<>();
        for (ControlFlow.Node node : flow.dominatorTree()) {
            nodes.put(node, new FlowNode(this, node, nodes));
        }
        return nodes.values();
    }

    public Collection<FlowNode> postDominatorTree() {
        Map<ControlFlow.Node, FlowNode> nodes = new LinkedHashMap<>();
        for (ControlFlow.Node node : flow.postDominatorTree()) {
            nodes.put(node, new FlowNode(this, node, nodes));
        }
        return nodes.values();
    }

    public FlowBlock findBlockByIndex(int index) {
        for (FlowBlock block : blocks) {
            if (block.startIndex() == index) {
                return block;
            }
        }
        return null;
    }
}
