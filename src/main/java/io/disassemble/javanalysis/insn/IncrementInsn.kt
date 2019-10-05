package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */

/**
 * A class representing a increment instruction.
 *
 * @param owner The [CtMethod] that this instruction is a part of.
 * @param index The index of this instruction in [CtMethod].
 */
class IncrementInsn(
        owner: CtMethod,
        index: Int
) : CtInsn(owner, index) {

    /**
     * The variable that is being incremented.
     */
    var variable
        get() = owner.code.iterator().byteAt(index + 1)
        set(value) {
            owner.code.iterator().writeByte(value, index + 1)
        }

    /**
     * The value to increment this variable by.
     */
    var increment
        get() = owner.code.iterator().signedByteAt(index + 2)
        set(value) {
            owner.code.iterator().writeByte(value, index + 2)
        }
}