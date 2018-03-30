package io.disassemble.javanalysis.insn

import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class IncrementInsn(
        owner: CtMethod,
        index: Int,
        opcode: Int,
        protected var variable: Int,
        protected var increment: Int
) : CtInsn(owner, index, opcode) {

    fun variable(): Int {
        return variable
    }

    fun increment(): Int {
        return increment
    }
}
