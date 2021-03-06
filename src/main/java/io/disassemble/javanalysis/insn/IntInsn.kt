package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import javassist.CtMethod
import javassist.bytecode.Opcode

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */

/**
 * A class representing a numeric instruction.
 *
 * @param owner The [CtMethod] that this instruction is a part of.
 * @param index The index of this instruction in [CtMethod].
 */
class IntInsn(
        owner: CtMethod,
        index: Int
) : CtInsn(owner, index) {

    /**
     * The value of this instruction.
     */
    var operand: Int
        get() {
            return if (opcode == Opcode.SIPUSH) {
                owner.code.iterator().u16bitAt(index + 1)
            } else {
                owner.code.iterator().byteAt(index + 1)
            }
        }
        set(operand) {
            if (operand <= 255) {
                opcode = Opcode.BIPUSH
                owner.code.iterator().writeByte(operand, index + 1)
            } else {
                opcode = Opcode.SIPUSH
                owner.code.iterator().write16bit(operand, index + 1)
            }
        }
}