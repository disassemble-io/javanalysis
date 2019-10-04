package io.disassemble.javanalysis.flow

import io.disassemble.javanalysis.insn.CtInsn
import io.disassemble.javanalysis.instructions
import javassist.CtMethod
import javassist.bytecode.analysis.ControlFlow

/**
 * @author Tyler Sedlar
 * @since 3/30/2018
 */

fun ControlFlow.Block.stripInsns(owner: CtMethod): List<CtInsn> {
    return owner.instructions.filter { it.index >= this.startIndex && it.index < this.endIndex }
}

val ControlFlow.Block.startIndex: Int
    get() = this.position()

val ControlFlow.Block.endIndex: Int
    get() = startIndex + length()