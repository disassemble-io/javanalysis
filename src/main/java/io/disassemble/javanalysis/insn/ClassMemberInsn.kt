package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
open class ClassMemberInsn(
        owner: CtMethod,
        index: Int
) : CtInsn(owner, index) {

    open var parent: String? = null
    open var name: String? = null
    open var desc: String? = null

    val nameAndTypeIndex: Int
        get() = owner.code.iterator().u16bitAt(index + 1)

    open val key: String
        get() = "$parent.$name$desc"
}
