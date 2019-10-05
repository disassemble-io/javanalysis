package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import io.disassemble.javanalysis.pool
import javassist.CtMethod
import javassist.bytecode.Opcode

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */

/**
 * A class representing a method instruction.
 *
 * @param owner The [CtMethod] that this instruction is a part of.
 * @param index The index of this instruction in [CtMethod].
 */
class MethodInsn(
        owner: CtMethod,
        index: Int
) : ClassMemberInsn(owner, index) {

    override var parent: String?
        get() = owner.pool.getMethodrefClassName(nameAndTypeIndex)
        set(value) {
            doMethodChange(value, name, desc)
        }

    override var name: String?
        get() = owner.pool.getMethodrefName(nameAndTypeIndex)
        set(value) {
            doMethodChange(parent, value, desc)
        }

    override var desc: String?
        get() = owner.pool.getMethodrefType(nameAndTypeIndex)
        set(value) {
            doMethodChange(parent, name, value)
        }

    /**
     * Called internally to change the values of associated [javassist.bytecode.ConstPool] data.
     *
     * @param parent The new parent to point at.
     * @param name The new name to point at.
     * @param desc The new descriptor to point at.
     */
    private fun doMethodChange(parent: String?, name: String?, desc: String?) {
        val newNAT = owner.pool.addNameAndTypeInfo(name, desc)
        val newCI = owner.pool.addClassInfo(parent)
        val poolRef: Int = if (opcode == Opcode.INVOKEINTERFACE) {
            owner.pool.addInterfaceMethodrefInfo(newCI, newNAT)
        } else {
            owner.pool.addMethodrefInfo(newCI, newNAT)
        }
        owner.code.iterator().write16bit(poolRef, index + 1)
    }
}
