package io.disassemble.javanalysis.flow

import io.disassemble.javanalysis.CtMethodNode
import io.disassemble.javanalysis.insn.CtInsn
import javassist.bytecode.analysis.ControlFlow
import java.util.*

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class FlowBlock(private val method: CtMethodNode, private val source: ControlFlow.Block) {

    private val instructions = ArrayList<CtInsn>()

    private var exits: List<FlowBlock?>? = null
    private var incoming: List<FlowBlock?>? = null

    fun source(): ControlFlow.Block {
        return source
    }

    fun startIndex(): Int {
        return source.position()
    }

    fun endIndex(): Int {
        return startIndex() + source.length()
    }

    fun instructions(): List<CtInsn> {
        return instructions
    }

    fun exits(): List<FlowBlock?> {
        return exits ?: emptyList()
    }

    fun exitIndices(): IntArray {
        exits?.let { exits ->
            val indices = IntArray(exits.size)
            for (i in indices.indices) {
                indices[i] = exits[i]?.startIndex() ?: -1
            }
            return indices
        }
        return IntArray(0)
    }

    fun incoming(): List<FlowBlock?> {
        return incoming ?: emptyList()
    }

    fun incomingIndices(): IntArray {
        incoming?.let { incoming ->

            val indices = IntArray(incoming.size)
            for (i in indices.indices) {
                // TODO: should -1 be the default?
                indices[i] = incoming[i]?.startIndex() ?: -1
            }
            return indices
        }
        // TODO: should this throw an exception?
        return IntArray(0)
    }

    fun catchers(): List<ControlFlow.Catcher> {
        return source.catchers().toList()
    }

    fun init(cfg: ControlFlowGraph) {
        if (exits != null) {
            return  // already initialized
        }
        constructInstructions(method.instructions())
        exits = constructExits(cfg)
        constructIncoming(cfg)
    }

    private fun constructInstructions(instructions: List<CtInsn>) {
        val start = method.normalizeIndex(startIndex())
        val end = method.normalizeIndex(endIndex())
        val offset = if (method.hasIndex(endIndex())) 0 else 1 // this occurs on the end source
        this.instructions.addAll(instructions.subList(start, end + offset))
    }

    private fun constructExits(cfg: ControlFlowGraph): List<FlowBlock?> {
        return (0 until source.exits())
                .map { translate(cfg, source.exit(it)) }
                .toList()
    }

    private fun constructIncoming(cfg: ControlFlowGraph): List<FlowBlock?> {
        return (0 until source.incomings())
                .map { translate(cfg, source.incoming(it)) }
                .toList()
    }

    companion object {
        fun translate(cfg: ControlFlowGraph, block: ControlFlow.Block): FlowBlock? {
            return cfg.findBlockByIndex(block.position())
        }
    }
}
