package io.disassemble.javanalysis.flow

import javassist.bytecode.analysis.ControlFlow

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 *
 * @param flow The [ControlFlow] for this graph to represent
 * @param blocks The list of [ControlFlow.Block]s within this graph
 */
class ControlFlowGraph(val flow: ControlFlow, val blocks: Array<ControlFlow.Block>) {

    /**
     * Retrieves the dominator tree of this [ControlFlowGraph].
     */
    val dominatorTree: Array<ControlFlow.Node>
        get() = flow.dominatorTree()

    /**
     * Retrieves the post dominator tree of this [ControlFlowGraph].
     */
    val postDominatorTree: Array<ControlFlow.Node>
        get() = flow.postDominatorTree()

    /**
     * Gets the block within this graph matching the given index.
     *
     * @param index The index of the block to find.
     *
     * @return The [ControlFlow.Block] within this graph matching the given index.
     */
    fun findBlockByIndex(index: Int): ControlFlow.Block ? {
        return blocks.firstOrNull { it.startIndex == index }
    }
}
