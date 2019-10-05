package io.disassemble.javanalysis

import io.disassemble.javanalysis.flow.ControlFlowGraph
import io.disassemble.javanalysis.flow.childBlocks
import io.disassemble.javanalysis.flow.stripInsns
import io.disassemble.javanalysis.insn.CtInsn
import io.disassemble.javanalysis.util.CodeParser
import javassist.CtClass
import javassist.CtMethod
import javassist.bytecode.CodeAttribute
import javassist.bytecode.ConstPool
import javassist.bytecode.MethodInfo
import javassist.bytecode.analysis.ControlFlow
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Tyler Sedlar
 * @since 5/19/2017
 */

private val codeMap: MutableMap<Int, List<CtInsn>> = HashMap()
private val indexMap: MutableMap<Int, HashMap<Int, Int>> = HashMap()

/**
 * Clears the cached data associated with this [CtMethod].
 */
fun CtMethod.unload() {
    val hash = hash
    codeMap.remove(hash)
    indexMap.remove(hash)
}

/**
 * Gets a unique hash for this [CtMethod].
 */
val CtMethod.hash: Int
    get() {
        return System.identityHashCode(this)
    }

/**
 * Gets a [List] of [CtInsn] within this [CtMethod].
 */
val CtMethod.instructions: List<CtInsn>
    get() {
        return if (hash in codeMap) {
            codeMap[hash]!!
        } else {
            val parsed = CodeParser.parse(this)
            codeMap[hash] = parsed
            parsed
        }
    }

/**
 * A map of the [CtInsn] indices associated with this [CtMethod].
 */
val CtMethod.indices: HashMap<Int, Int>
    get() {
        return if (hash in indexMap) {
            indexMap[hash]!!
        } else {
            val map = HashMap<Int, Int>()
            indexMap[hash] = map
            map
        }
    }

/**
 * The [CtClass] that this [CtMethod] is a part of.
 */
val CtMethod.owner: CtClass
    get() = this.declaringClass

/**
 * The descriptor of this [CtMethod].
 */
val CtMethod.desc: String
    get() = this.signature

/**
 * A string formed as <owner.name>.<name><desc>
 */
val CtMethod.key: String
    get() = "${owner.name}.$name$desc"

/**
 * Gets the [MethodInfo] associated with this [CtMethod].
 */
val CtMethod.info: MethodInfo
    get() = this.methodInfo

/**
 * Gets the [CodeAttribute] associated with this [CtMethod].
 */
val CtMethod.code: CodeAttribute
    get() = info.codeAttribute

/**
 * Gets the [ConstPool] associated with this [CtMethod].
 */
val CtMethod.pool: ConstPool
    get() = info.constPool

/**
 * Gets the line that this [CtMethod] starts on.
 */
val CtMethod.line: Int
    get() = instructions[0].line - 1

/**
 * Gets the [ControlFlowGraph] of this [CtMethod].
 */
val CtMethod.cfg: ControlFlowGraph
    get() {
        try {
            val flow = ControlFlow(this.owner, this.info)
            val blocks = flow.basicBlocks()
            return ControlFlowGraph(flow, blocks)
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalStateException("Invalid bytecode for cfg in $longName")
        }
    }

/**
 * Maps the instruction index to the given position.
 *
 * @param insnIndex The index of the [CtInsn] (map key).
 * @param position The position of the [CtInsn] (map value).
 */
fun CtMethod.index(insnIndex: Int, position: Int) {
    indices[insnIndex] = position
}

/**
 * Gets the index of the given [CtInsn] within this [CtMethod].
 *
 * @param insn The [CtInsn] to get an index for.
 *
 * @return The index of the given [CtInsn] within this [CtMethod].
 */
fun CtMethod.indexOf(insn: CtInsn): Int {
    return normalizeIndex(insn.index)
}

/**
 * Gets the index if it appears within [indices], else the previous one.
 *
 * @param index The index to normalize.
 *
 * @return The index if it appears within [indices], else the previous one.
 */
fun CtMethod.normalizeIndex(index: Int): Int {
    return if (index in indices) {
        indices[index]
    } else {
        indices[index - 1]
    } ?: error("Unable to normalize index: $index")
}

/**
 * Checks if the given index is within [indices].
 *
 * @param index The index to search for.
 *
 * @return true if the index is within [indices], otherwise false.
 */
fun CtMethod.hasIndex(index: Int): Boolean {
    return indices.containsKey(index)
}

/**
 * Runs the given callback recursively on each of the [ControlFlow.Node]'s [ControlFlow.Block]s.
 *
 * @param visited A list of already visited block indices.
 * @param node The [ControlFlow.Node] of whose [ControlFlow.Block]s to visit.
 * @param callback The action to run on each [ControlFlow.Block].
 */
private fun visitRecursive(visited: MutableSet<Int>, node: ControlFlow.Node, callback: (block: ControlFlow.Block) -> Unit) {
    node.block().let { block ->
        val idx = block.index()
        if (idx !in visited) {
            visited.add(idx)
            callback(block)
            node.childBlocks.forEach { visitRecursive(visited, it, callback) }
        }
    }
}

/**
 * A flattened list of this method's [ControlFlowGraph.postDominatorTree].
 */
val CtMethod.flatPDT: MutableList<CtInsn>
    get() {
        val blocks: MutableList<ControlFlow.Block> = ArrayList()
        val visited: MutableSet<Int> = HashSet()

        this.cfg.postDominatorTree.forEach { node ->
            visitRecursive(visited, node) { blocks.add(it) }
        }

        return blocks.flatMap { it.stripInsns(this) }.toMutableList()
    }

/**
 * A flattened list of this method's [ControlFlowGraph.dominatorTree].
 */
val CtMethod.flatDT: MutableList<CtInsn>
    get() {
        val blocks: MutableList<ControlFlow.Block> = ArrayList()
        val visited: MutableSet<Int> = HashSet()

        this.cfg.dominatorTree.forEach { node ->
            visitRecursive(visited, node) { blocks.add(it) }
        }

        return blocks.flatMap { it.stripInsns(this) }.toMutableList()
    }
