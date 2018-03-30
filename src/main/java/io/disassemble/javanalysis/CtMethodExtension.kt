package io.disassemble.javanalysis

import io.disassemble.javanalysis.flow.ControlFlowGraph
import io.disassemble.javanalysis.flow.FlowBlock
import io.disassemble.javanalysis.insn.CtInsn
import io.disassemble.javanalysis.util.CodeParser
import javassist.CtClass
import javassist.CtMethod
import javassist.bytecode.CodeAttribute
import javassist.bytecode.ConstPool
import javassist.bytecode.MethodInfo
import javassist.bytecode.analysis.ControlFlow

/**
 * @author Tyler Sedlar
 * @since 5/19/2017
 */

object CtMethodExtension {
    internal val codeMap: MutableMap<CtMethod, List<CtInsn>> = HashMap()
    internal val indexMap: MutableMap<CtMethod, HashMap<Int, Int>> = HashMap()
}

val CtMethod.instructions: List<CtInsn>
    get() {
        return if (CtMethodExtension.codeMap.containsKey(this)) {
            CtMethodExtension.codeMap[this]!!
        } else {
            val parsed = CodeParser.parse(this)
            CtMethodExtension.codeMap[this] = parsed
            parsed
        }
    }

val CtMethod.indices: HashMap<Int, Int>
    get() {
        return if (CtMethodExtension.indexMap.containsKey(this)) {
            CtMethodExtension.indexMap[this]!!
        } else {
            val map = HashMap<Int, Int>()
            CtMethodExtension.indexMap[this] = map
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
    get() = instructions[0].line() - 1

val CtMethod.cfg: ControlFlowGraph
    get() {
        try {
            val flow = ControlFlow(this.owner, this.info)
            val blocks = flow.basicBlocks().map { FlowBlock(this, it) }
            val cfg = ControlFlowGraph(flow, blocks)
            blocks.forEach { block -> block.init(cfg) }
            return cfg
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalStateException("Invalid bytecode for cfg in $longName")
        }
    }

fun CtMethod.index(insnIndex: Int, position: Int) {
    indices[insnIndex] = position
}

fun CtMethod.indexOf(insn: CtInsn): Int {
    return normalizeIndex(insn.index())
}

fun CtMethod.normalizeIndex(index: Int): Int {
    return if (index in indices) {
        indices[index]
    } else {
        indices[index - 1]
    } ?: index
}

fun CtMethod.hasIndex(index: Int): Boolean {
    return indices.containsKey(index)
}
