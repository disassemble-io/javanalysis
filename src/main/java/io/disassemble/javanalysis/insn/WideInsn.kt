package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class WideInsn(
        owner: CtMethod,
        index: Int
) : CtInsn(owner, index) {

    var wideOp: Int
        get() = owner.code.iterator().byteAt(index + 1)
        set(value) = owner.code.iterator().writeByte(value, index + 1)

    var wideIndex: Int
        get() = owner.code.iterator().u16bitAt(index + 2)
        set(value) = owner.code.iterator().write16bit(value, index + 2)
}