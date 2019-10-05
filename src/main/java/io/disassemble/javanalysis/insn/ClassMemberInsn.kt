package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */

/**
 * A class representing a class member (field or method instruction).
 *
 * @param owner The [CtMethod] that this instruction is a part of.
 * @param index The index of this instruction in [CtMethod].
 */
open class ClassMemberInsn(
        owner: CtMethod,
        index: Int
) : CtInsn(owner, index) {

    /**
     * The parent member this instruction is pointed at.
     */
    open var parent: String? = null

    /**
     * The name of this member.
     */
    open var name: String? = null

    /**
     * The descriptor of this member.
     */
    open var desc: String? = null

    /**
     * The nameAndTypeIndex internally used with [javassist.bytecode.CodeIterator].
     */
    val nameAndTypeIndex: Int
        get() = owner.code.iterator().u16bitAt(index + 1)

    /**
     * A string in the format of <parent>.<name><desc>.
     */
    open val key: String
        get() = "$parent.$name$desc"
}
