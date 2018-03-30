package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.info
import io.disassemble.javanalysis.pool
import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class LdcInsn(
        owner: CtMethod,
        index: Int,
        opcode: Int,
        val poolIndex: Int
) : CtInsn(owner, index, opcode) {

    val tag: Int
        get() = owner.info.constPool.getTag(poolIndex)

    val cst: Any
        get() = owner.pool.getLdcValue(poolIndex)
}
