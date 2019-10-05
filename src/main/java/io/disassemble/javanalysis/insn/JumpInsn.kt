package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import javassist.CtMethod
import javassist.bytecode.Opcode

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */

/**
 * A class representing a jump instruction.
 *
 * @param owner The [CtMethod] that this instruction is a part of.
 * @param index The index of this instruction in [CtMethod].
 */
class JumpInsn(
        owner: CtMethod,
        index: Int
) : CtInsn(owner, index) {

    /**
     * The position to jump to.
     */
    val target: Int
        get() {
            return if (opcode == Opcode.RET) {
                owner.code.iterator().byteAt(index + 1)
            } else if (opcode == Opcode.GOTO_W || opcode == Opcode.JSR_W) {
                owner.code.iterator().s32bitAt(index + 1) + index
            } else {
                owner.code.iterator().s16bitAt(index + 1) + index
            }
        }
}