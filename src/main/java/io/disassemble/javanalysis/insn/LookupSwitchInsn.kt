package io.disassemble.javanalysis.insn

import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class LookupSwitchInsn(
        owner: CtMethod,
        index: Int,
        opcode: Int,
        val defaultIndex: Int,
        val keys: IntArray,
        val indices: IntArray
) : CtInsn(owner, index, opcode) {

    val size: Int
        get() = keys.size
}
