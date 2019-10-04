package io.disassemble.javanalysis.flow

import javassist.bytecode.analysis.ControlFlow

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class ControlFlowGraph(val flow: ControlFlow, val blocks: Array<ControlFlow.Block>) {

    val dominatorTree: Array<ControlFlow.Node>
        get() = flow.dominatorTree()

    val postDominatorTree: Array<ControlFlow.Node>
        get() = flow.postDominatorTree()

    fun findBlockByIndex(index: Int): ControlFlow.Block ? {
        return blocks.firstOrNull { it.startIndex == index }
    }
}
