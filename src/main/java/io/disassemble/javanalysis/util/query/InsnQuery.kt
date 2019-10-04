package io.disassemble.javanalysis.util.query

import io.disassemble.javanalysis.insn.CtInsn
import io.disassemble.javanalysis.insn.IntInsn
import javassist.bytecode.Opcode

class InsnQuery {

    var name: String? = null
    var dist = 1
    var conditions = ArrayList<(CtInsn) -> Boolean>()

    fun name(name: String): InsnQuery {
        this.name = name
        return this
    }

    fun dist(dist: Int): InsnQuery {
        this.dist = dist
        return this
    }

    fun opcode(vararg opcodes: Int): InsnQuery {
        conditions.add { opcodes.contains(it.opcode) }
        return this
    }

    fun int(operand: Int): InsnQuery {
        conditions.add { it is IntInsn && it.operand == operand }
        return this
    }

    fun check(insn: CtInsn): Boolean {
        for (condition in conditions) {
            if (!(condition(insn))) {
                return false
            }
        }
        return true
    }
}

val sipush: InsnQuery
    get() = InsnQuery().apply {
        opcode(Opcode.SIPUSH)
    }