package io.disassemble.javanalysis.insn

import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */

/**
 * A class representing a multianewarray instruction.
 *
 * @param owner The [CtMethod] that this instruction is a part of.
 * @param index The index of this instruction in [CtMethod].
 */
class MultiANewArrayInsn(
        owner: CtMethod,
        index: Int
) : TypeInsn(owner, index) {

    /**
     * How many dimensions within this [MultiANewArrayInsn].
     */
    val dimensions: Int
        get() {
            var dim = 0
            val desc = type
            while (desc[dim] == '[') {
                ++dim
            }
            return dim
        }
}