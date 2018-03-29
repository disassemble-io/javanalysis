package io.disassemble.javanalysis.flow

import javassist.bytecode.analysis.ControlFlow

/**
 * @author Tyler Sedlar
 * @since 5/21/2017
 */
class FlowNode(
        private val cfg: ControlFlowGraph,
        private val source: ControlFlow.Node,
        private val mapping: Map<ControlFlow.Node, FlowNode>
) {

    fun source(): ControlFlow.Node {
        return source
    }

    fun parent(): FlowNode? {
        return mapping[source.parent()]
    }

    fun block(): FlowBlock? {
        return FlowBlock.translate(cfg, source.block())
    }

    fun children(): List<FlowNode?> {
        return (0 until source.children())
                .map { mapping[source.child(it)] }
    }
}
