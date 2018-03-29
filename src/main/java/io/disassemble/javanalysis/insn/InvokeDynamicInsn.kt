package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.CtMethodNode

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class InvokeDynamicInsn(
        owner: CtMethodNode,
        index: Int,
        opcode: Int,
        protected var name: String,
        protected var type: String,
        protected var bootstrap: Int
) : CtInsn(owner, index, opcode) {

    fun name(): String {
        return name
    }

    fun type(): String {
        return type
    }

    fun bootstrap(): Int {
        return bootstrap
    }
}
