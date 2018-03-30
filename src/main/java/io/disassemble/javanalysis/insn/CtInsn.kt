package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.*
import javassist.CtMethod
import javassist.bytecode.Mnemonic
import java.util.concurrent.atomic.AtomicReference

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
open class CtInsn(
        protected val owner: CtMethod,
        protected var index: Int,
        protected var opcode: Int
) {

    val previous = AtomicReference<CtInsn>()
    val next = AtomicReference<CtInsn>()

    fun owner(): CtMethod {
        return owner
    }

    fun index(): Int {
        return index
    }

    fun opcode(): Int {
        return opcode
    }

    fun opname(): String {
        return Mnemonic.OPCODE[opcode]
    }

    fun line(): Int {
        return owner.info.getLineNumber(index)
    }

    fun relativeLine(): Int {
        return line() - owner.line
    }

    fun position(): Int {
        return owner.indexOf(this)
    }

    fun hasPrevious(): Boolean {
        return previous.get() != null
    }

    operator fun hasNext(): Boolean {
        return next.get() != null
    }

    override fun toString(): String {
        return opname()
    }
}
