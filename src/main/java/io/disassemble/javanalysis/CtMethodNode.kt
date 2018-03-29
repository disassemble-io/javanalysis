package io.disassemble.javanalysis

import io.disassemble.javanalysis.flow.ControlFlowGraph
import io.disassemble.javanalysis.flow.FlowBlock
import io.disassemble.javanalysis.insn.CtInsn
import io.disassemble.javanalysis.util.CodeParser
import javassist.CtMethod
import javassist.bytecode.CodeAttribute
import javassist.bytecode.ConstPool
import javassist.bytecode.MethodInfo
import javassist.bytecode.analysis.ControlFlow
import java.util.*

/**
 * @author Tyler Sedlar
 * @since 5/19/2017
 */
class CtMethodNode(private val source: CtMethod, private val owner: CtClassNode) {
    private val instructions: List<CtInsn> by lazy { CodeParser.parse(this) }

    private val indices = HashMap<Int, Int>()

    fun source(): CtMethod {
        return source
    }

    fun owner(): CtClassNode {
        return owner
    }

    fun name(): String {
        return source.name
    }

    fun desc(): String {
        return source.signature
    }

    fun key(): String {
        return "${owner.name()}.${name()}${desc()}"
    }

    fun info(): MethodInfo {
        return source.methodInfo
    }

    fun code(): CodeAttribute {
        return info().codeAttribute
    }

    fun pool(): ConstPool {
        return info().constPool
    }

    fun instructions(): List<CtInsn> {
        return instructions
    }

    fun line(): Int {
        return instructions()[0].line() - 1
    }

    fun index(insnIndex: Int, position: Int) {
        indices[insnIndex] = position
    }

    fun indexOf(insn: CtInsn): Int {
        return normalizeIndex(insn.index())
    }

    fun normalizeIndex(index: Int): Int {
        return if (index in indices) {
            indices[index]
        } else {
            indices[index - 1]
        } ?: error("Unable to normalize index: $index")
    }

    fun hasIndex(index: Int): Boolean {
        return indices.containsKey(index)
    }

    fun cfg(): ControlFlowGraph {
        try {
            val flow = ControlFlow(owner.source(), info())
            val blocks = flow.basicBlocks().map { FlowBlock(this, it) }
            val cfg = ControlFlowGraph(flow, blocks)
            blocks.forEach { block -> block.init(cfg) }
            return cfg
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalStateException("Invalid bytecode for cfg in " + source.longName)
        }

    }
}
