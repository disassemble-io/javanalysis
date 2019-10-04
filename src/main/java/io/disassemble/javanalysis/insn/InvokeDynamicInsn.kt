package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import io.disassemble.javanalysis.pool
import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class InvokeDynamicInsn(
        owner: CtMethod,
        index: Int
) : CtInsn(owner, index) {

    private var poolIndex = owner.code.iterator().u16bitAt(index + 1)

    var type: String
        get() = owner.pool.getInvokeDynamicType(poolIndex)
        set(value) {
            _nameAndType = owner.pool.addNameAndTypeInfo(value, value)
            poolIndex = owner.pool.addInvokeDynamicInfo(bootstrap, _nameAndType)
            owner.code.iterator().write16bit(poolIndex, index + 1)
        }

    var bootstrap: Int
        get() = owner.pool.getInvokeDynamicBootstrap(poolIndex)
        set(value) {
            poolIndex = owner.pool.addInvokeDynamicInfo(value, nameAndType)
            owner.code.iterator().write16bit(poolIndex, index + 1)
        }

    private var _nameAndType = -1

    private val nameAndType: Int
        get() = if (_nameAndType == -1) owner.pool.getInvokeDynamicNameAndType(poolIndex) else _nameAndType
}