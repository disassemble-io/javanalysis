package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class IncrementInsn(
        owner: CtMethod,
        index: Int
) : CtInsn(owner, index) {

    var variable
        get() = owner.code.iterator().byteAt(index + 1)
        set(value) {
            owner.code.iterator().writeByte(value, index + 1)
        }

    var increment
        get() = owner.code.iterator().signedByteAt(index + 2)
        set(value) {
            owner.code.iterator().writeByte(value, index + 2)
        }
}