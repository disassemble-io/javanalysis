package io.disassemble.javanalysis.flow

import io.disassemble.javanalysis.hasIndex
import io.disassemble.javanalysis.insn.CtInsn
import io.disassemble.javanalysis.instructions
import io.disassemble.javanalysis.normalizeIndex
import javassist.CtMethod
import javassist.bytecode.analysis.ControlFlow
import java.util.*

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class FlowBlock(val method: CtMethod, val source: ControlFlow.Block) {

    val instructions = ArrayList<CtInsn>()

    private var outgoingEdges: List<FlowBlock?>? = null
    private var incomingEdges: List<FlowBlock?>? = null

    val startIndex: Int
        get() = source.position()

    val endIndex: Int
        get() = startIndex + source.length()

    val exits: List<FlowBlock?>
        get() = outgoingEdges ?: emptyList()

    val exitIndices: IntArray
        get() {
            outgoingEdges?.let { exits ->
                val indices = IntArray(exits.size)
                for (i in indices.indices) {
                    indices[i] = exits[i]?.startIndex ?: -1
                }
                return indices
            }
            return IntArray(0)
        }

    val incoming: List<FlowBlock?>
        get() = incomingEdges ?: emptyList()

    val incomingIndices: IntArray
        get() {
            incomingEdges?.let { incoming ->
                val indices = IntArray(incoming.size)
                for (i in indices.indices) {
                    // TODO: should -1 be the default?
                    indices[i] = incoming[i]?.startIndex ?: -1
                }
                return indices
            }
            // TODO: should this throw an exception?
            return IntArray(0)
        }

    val catchers: List<ControlFlow.Catcher>
        get() = source.catchers().toList()

    fun init(cfg: ControlFlowGraph) {
        if (outgoingEdges != null) {
            return  // already initialized
        }
        constructInstructions(method.instructions)
        outgoingEdges = constructExits(cfg)
        constructIncoming(cfg)
    }

    private fun constructInstructions(instructions: List<CtInsn>) {
        val start = method.normalizeIndex(startIndex)
        val end = method.normalizeIndex(endIndex)
        val offset = if (method.hasIndex(endIndex)) 0 else 1 // this occurs on the end source
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
