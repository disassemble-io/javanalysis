package io.disassemble.javanalysis.flow

import javassist.bytecode.analysis.ControlFlow

/**
 * @author Tyler Sedlar
 * @since 3/30/2018
 */

/**
 * Gets the child blocks that belong to this [ControlFlow.Node].
 */
val ControlFlow.Node.childBlocks: List<ControlFlow.Node>
    get() {
        val childList: MutableList<ControlFlow.Node> = ArrayList()
        for (i in 0 until this.children()) {
            childList.add(this.child(i))
        }
        return childList
    }