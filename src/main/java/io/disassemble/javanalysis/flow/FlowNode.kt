package io.disassemble.javanalysis.flow

import javassist.bytecode.analysis.ControlFlow

/**
 * @author Tyler Sedlar
 * @since 5/21/2017
 */
class FlowNode(
        private val cfg: ControlFlowGraph,
        val source: ControlFlow.Node,
        private val mapping: Map<ControlFlow.Node, FlowNode>
) {

    val parent: FlowNode?
        get() = mapping[source.parent()]

    val block: FlowBlock?
        get() = FlowBlock.translate(cfg, source.block())

    val children: List<FlowNode?>
        get() = (0 until source.children()).map { mapping[source.child(it)] }
}