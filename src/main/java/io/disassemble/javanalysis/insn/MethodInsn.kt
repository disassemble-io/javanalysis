package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import io.disassemble.javanalysis.pool
import javassist.CtMethod
import javassist.bytecode.Opcode

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
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
