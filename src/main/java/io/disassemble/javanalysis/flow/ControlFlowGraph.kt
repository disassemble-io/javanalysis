package io.disassemble.javanalysis.flow

import javassist.bytecode.analysis.ControlFlow

import java.util.*

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class ControlFlowGraph(private val flow: ControlFlow, private val blocks: List<FlowBlock>) {

    fun flow(): ControlFlow {
        return flow
    }

    fun blocks(): List<FlowBlock> {
        return blocks
    }

    fun dominatorTree(): Collection<FlowNode> {
        val nodes = LinkedHashMap<ControlFlow.Node, FlowNode>()
        for (node in flow.dominatorTree()) {
            nodes[node] = FlowNode(this, node, nodes)
        }
        return nodes.values
    }

    fun postDominatorTree(): Collection<FlowNode> {
        val nodes = LinkedHashMap<ControlFlow.Node, FlowNode>()
        for (node in flow.postDominatorTree()) {
            nodes[node] = FlowNode(this, node, nodes)
        }
        return nodes.values
    }

    fun findBlockByIndex(index: Int): FlowBlock? {
        return blocks.firstOrNull { it.startIndex() == index }
    }
}
