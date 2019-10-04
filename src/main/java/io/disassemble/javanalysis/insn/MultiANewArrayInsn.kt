package io.disassemble.javanalysis.insn

import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class MultiANewArrayInsn(
        owner: CtMethod,
        index: Int
) : TypeInsn(owner, index) {

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