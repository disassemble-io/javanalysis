package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */

/**
 * A class representing a wide instruction.
 *
 * @param owner The [CtMethod] that this instruction is a part of.
 * @param index The index of this instruction in [CtMethod].
 */
class WideInsn(
        owner: CtMethod,
        index: Int
) : CtInsn(owner, index) {

    /**
     * The opcode to be widened.
     */
    var wideOp: Int
        get() = owner.code.iterator().byteAt(index + 1)
        set(value) = owner.code.iterator().writeByte(value, index + 1)

    /**
     * The index of the widened instruction.
     */
    var wideIndex: Int
        get() = owner.code.iterator().u16bitAt(index + 2)
        set(value) = owner.code.iterator().write16bit(value, index + 2)
}