package io.disassemble.javanalysis.flow;

import javassist.bytecode.analysis.ControlFlow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Tyler Sedlar
 * @since 5/21/2017
 */
public class FlowNode {

    private final ControlFlowGraph cfg;
    private final ControlFlow.Node source;

    private final Map<ControlFlow.Node, FlowNode> mapping;

    public FlowNode(ControlFlowGraph cfg, ControlFlow.Node source, Map<ControlFlow.Node, FlowNode> mapping) {
        this.cfg = cfg;
        this.source = source;
        this.mapping = mapping;
    }

    public ControlFlow.Node source() {
        return source;
    }

    public FlowNode parent() {
        return mapping.get(source.parent());
    }

    public FlowBlock block() {
        return FlowBlock.translate(cfg, source.block());
    }

    public FlowNode[] children() {
        FlowNode[] children = new FlowNode[source.children()];
        for (int i = 0; i < children.length; i++) {
            children[i] = mapping.get(source.child(i));
        }
        return children;
    }
}
