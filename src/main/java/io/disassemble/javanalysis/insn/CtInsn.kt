package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.indexOf
import io.disassemble.javanalysis.info
import io.disassemble.javanalysis.line
import io.disassemble.javanalysis.util.InsnUtil
import javassist.CtMethod
import javassist.bytecode.Mnemonic
import java.util.concurrent.atomic.AtomicReference

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
open class CtInsn(val owner: CtMethod, val index: Int, val opcode: Int) {

    val previous = AtomicReference<CtInsn>()
    val next = AtomicReference<CtInsn>()

    val opname: String
        get() = Mnemonic.OPCODE[opcode]

    val line: Int
        get() = owner.info.getLineNumber(index)

    val relativeLine: Int
        get() = line - owner.line

    val position: Int
        get() = owner.indexOf(this)

    val CtInsn.verbose: String
        get() = InsnUtil.stringify(this)

    fun hasPrevious(): Boolean = previous.get() != null

    operator fun hasNext(): Boolean = next.get() != null

    override fun toString(): String = opname
}
