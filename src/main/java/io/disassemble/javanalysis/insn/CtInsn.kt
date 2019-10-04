package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import io.disassemble.javanalysis.indexOf
import io.disassemble.javanalysis.info
import io.disassemble.javanalysis.line
import io.disassemble.javanalysis.util.InsnUtil
import javassist.CtMethod
import javassist.bytecode.Mnemonic

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
open class CtInsn(val owner: CtMethod, var index: Int) {

    var previous: CtInsn? = null
    var next: CtInsn? = null

    internal var growOffset = 0

    var opcode: Int
        get() = owner.code.iterator().byteAt(index)
        set(value) = owner.code.iterator().writeByte(value, index)

    val opname: String
        get() = Mnemonic.OPCODE[opcode]

    val line: Int
        get() = owner.info.getLineNumber(index)

    val relativeLine: Int
        get() = line - owner.line

    val position: Int
        get() = owner.indexOf(this)

    val verbose: String
        get() = InsnUtil.stringify(this)

    fun hasPrevious(): Boolean = previous != null

    operator fun hasNext(): Boolean = next != null

    override fun toString(): String = opname
}
