package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import io.disassemble.javanalysis.pool
import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */

/**
 * A class representing a field instruction.
 *
 * @param owner The [CtMethod] that this instruction is a part of.
 * @param index The index of this instruction in [CtMethod].
 */
class FieldInsn(
        owner: CtMethod,
        index: Int
) : ClassMemberInsn(owner, index) {

    override val key: String
        get() = "$parent.$name"

    override var parent: String?
        get() = owner.pool.getFieldrefClassName(nameAndTypeIndex)
        set(value) {
            doFieldChange(value, name, desc)
        }

    override var name: String?
        get() = owner.pool.getFieldrefName(nameAndTypeIndex)
        set(value) {
            doFieldChange(parent, value, desc)
        }

    override var desc: String?
        get() = owner.pool.getFieldrefType(nameAndTypeIndex)
        set(value) {
            doFieldChange(parent, name, value)
        }

    /**
     * Called internally to change the values of associated [javassist.bytecode.ConstPool] data.
     *
     * @param parent The new parent to point at.
     * @param name The new name to point at.
     * @param desc The new descriptor to point at.
     */
    private fun doFieldChange(parent: String?, name: String?, desc: String?) {
        val newNAT = owner.pool.addNameAndTypeInfo(name, desc)
        val newCI = owner.pool.addClassInfo(parent)
        val poolRef = owner.pool.addFieldrefInfo(newCI, newNAT)
        owner.code.iterator().write16bit(poolRef, index + 1)
    }
}
