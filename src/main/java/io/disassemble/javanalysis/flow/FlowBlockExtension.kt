package io.disassemble.javanalysis.flow

import io.disassemble.javanalysis.insn.CtInsn
import io.disassemble.javanalysis.instructions
import javassist.CtMethod
import javassist.bytecode.analysis.ControlFlow

/**
 * @author Tyler Sedlar
 * @since 3/30/2018
 */

/**
 * Gets the instructions within this [ControlFlow.Block].
 */
fun ControlFlow.Block.stripInsns(owner: CtMethod): List<CtInsn> {
    return owner.instructions.filter { it.index >= this.startIndex && it.index < this.endIndex }
}

/**
 * Gets the start index of this [ControlFlow.Block].
 */
val ControlFlow.Block.startIndex: Int
    get() = this.position()

/**
 * Gets the end index of this [ControlFlow.Block].
 */
val ControlFlow.Block.endIndex: Int
    get() = startIndex + length()