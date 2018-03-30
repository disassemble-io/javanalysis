package io.disassemble.javanalysis.flow

import javassist.bytecode.analysis.ControlFlow

import java.util.*

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class ControlFlowGraph(val flow: ControlFlow, val blocks: List<FlowBlock>) {

    val dominatorTree: Collection<FlowNode>
        get() {
            val nodes = LinkedHashMap<ControlFlow.Node, FlowNode>()
            for (node in flow.dominatorTree()) {
                nodes[node] = FlowNode(this, node, nodes)
            }
            return nodes.values
        }

    val postDominatorTree: Collection<FlowNode>
        get() {
            val nodes = LinkedHashMap<ControlFlow.Node, FlowNode>()
            for (node in flow.postDominatorTree()) {
                nodes[node] = FlowNode(this, node, nodes)
            }
            return nodes.values
        }

    fun findBlockByIndex(index: Int): FlowBlock? {
        return blocks.firstOrNull { it.startIndex == index }
    }
}
