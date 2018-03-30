package io.disassemble.javanalysis.flow

import javassist.bytecode.analysis.ControlFlow

/**
 * @author Tyler Sedlar
 * @since 3/30/2018
 */

val ControlFlow.Node.childBlocks: List<ControlFlow.Node>
    get() {
        val childList: MutableList<ControlFlow.Node> = ArrayList()
        for (i in 0 until this.children()) {
            childList.add(this.child(i))
        }
        return childList
    }