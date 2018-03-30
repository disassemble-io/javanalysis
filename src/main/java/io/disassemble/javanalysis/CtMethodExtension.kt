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

val CtMethod.hash: Int
    get() {
        return System.identityHashCode(this)
    }

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

val CtMethod.owner: CtClass
    get() = this.declaringClass

val CtMethod.desc: String
    get() = this.signature

val CtMethod.key: String
    get() = "${owner.name}.$name$desc"

val CtMethod.info: MethodInfo
    get() = this.methodInfo

val CtMethod.code: CodeAttribute
    get() = info.codeAttribute

val CtMethod.pool: ConstPool
    get() = info.constPool

val CtMethod.line: Int
    get() = instructions[0].line - 1

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

fun CtMethod.index(insnIndex: Int, position: Int) {
    indices[insnIndex] = position
}

fun CtMethod.indexOf(insn: CtInsn): Int {
    return normalizeIndex(insn.index)
}

fun CtMethod.normalizeIndex(index: Int): Int {
    return if (index in indices) {
        indices[index]
    } else {
        indices[index - 1]
    } ?: error("Unable to normalize index: $index")
}

fun CtMethod.hasIndex(index: Int): Boolean {
    return indices.containsKey(index)
}

private fun visitRecursive(visited: MutableSet<Int>, node: ControlFlow.Node, callback: (block: ControlFlow.Block) -> Unit) {
    node.block().let {
        val idx = it.index()
        if (idx !in visited) {
            visited.add(idx)
            callback(it)
            node.childBlocks.forEach { visitRecursive(visited, it, callback) }
        }
    }
}

val CtMethod.flatPDT: MutableList<CtInsn>
    get() {
        val blocks: MutableList<ControlFlow.Block> = ArrayList()
        val visited: MutableSet<Int> = HashSet()

        this.cfg.postDominatorTree?.forEach {
            visitRecursive(visited, it, { blocks.add(it) })
        }

        return blocks.flatMap { it.stripInsns(this) }.toMutableList()
    }

val CtMethod.flatDT: MutableList<CtInsn>
    get() {
        val blocks: MutableList<ControlFlow.Block> = ArrayList()
        val visited: MutableSet<Int> = HashSet()

        this.cfg.dominatorTree?.forEach {
            visitRecursive(visited, it, { blocks.add(it) })
        }

        return blocks.flatMap { it.stripInsns(this) }.toMutableList()
    }
