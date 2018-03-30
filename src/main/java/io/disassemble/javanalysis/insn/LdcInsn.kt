package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.*
import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class LdcInsn(
        owner: CtMethod,
        index: Int,
        opcode: Int,
        protected var poolIndex: Int
) : CtInsn(owner, index, opcode) {

    fun poolIndex(): Int {
        return poolIndex
    }

    fun tag(): Int {
        return owner.info.constPool.getTag(poolIndex())
    }

    fun cst(): Any {
        return owner.pool.getLdcValue(poolIndex())
    }
}
