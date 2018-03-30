package io.disassemble.javanalysis.util

import io.disassemble.javanalysis.code
import io.disassemble.javanalysis.index
import io.disassemble.javanalysis.insn.*
import io.disassemble.javanalysis.pool
import javassist.CtMethod
import javassist.bytecode.BadBytecode
import javassist.bytecode.Opcode.*
import java.util.*

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
object CodeParser {

    fun parse(method: CtMethod): List<CtInsn> {
        try {
            val instructions = ArrayList<CtInsn>()
            val pool = method.pool
            val attr = method.code
            val itr = attr.iterator()
            var previous: CtInsn? = null
            while (itr.hasNext()) {
                var index = itr.next()
                var opcode = itr.byteAt(index)
                var poolIndex = index + 1
                val insn: CtInsn
                when {
                    InsnUtil.isLdcInsn(opcode) -> {
                        val wide = opcode == LDC_W || opcode == LDC2_W
                        poolIndex = if (wide) itr.u16bitAt(poolIndex) else itr.byteAt(poolIndex)
                        insn = LdcInsn(method, index, opcode, poolIndex)
                    }
                    InsnUtil.isIntInsn(opcode) -> {
                        val s16 = opcode == SIPUSH
                        val operand = if (s16) itr.s16bitAt(poolIndex) else itr.byteAt(poolIndex)
                        insn = IntInsn(method, index, opcode, operand)
                    }
                    InsnUtil.isVarInsn(opcode) -> {
                        val varb = if (InsnUtil.isUnderscoreInsn(opcode)) InsnUtil.underVal(opcode) else
                            itr.byteAt(poolIndex)
                        insn = VarInsn(method, index, opcode, varb)
                    }
                    InsnUtil.isJumpInsn(opcode) -> {
                        val wide = opcode == GOTO_W || opcode == JSR_W
                        val target = (if (wide) itr.s32bitAt(poolIndex) else itr.s16bitAt(poolIndex)) + index
                        insn = JumpInsn(method, index, opcode, target)
                    }
                    InsnUtil.isIncrementInsn(opcode) -> {
                        val varb = itr.byteAt(poolIndex)
                        val increment = itr.signedByteAt(poolIndex + 1)
                        insn = IncrementInsn(method, index, opcode, varb, increment)
                    }
                    InsnUtil.isFieldInsn(opcode) -> {
                        poolIndex = itr.u16bitAt(poolIndex)
                        val parent = pool.getFieldrefClassName(poolIndex)
                        val name = pool.getFieldrefName(poolIndex)
                        val desc = pool.getFieldrefType(poolIndex)
                        insn = FieldInsn(method, index, opcode, parent, name, desc)
                    }
                    InsnUtil.isMethodInsn(opcode) -> {
                        poolIndex = itr.u16bitAt(poolIndex)
                        insn = if (opcode == INVOKEINTERFACE) {
                            val parent = pool.getInterfaceMethodrefClassName(poolIndex)
                            val name = pool.getInterfaceMethodrefName(poolIndex)
                            val desc = pool.getInterfaceMethodrefType(poolIndex)
                            MethodInsn(method, index, opcode, parent, name, desc)
                        } else {
                            val parent = pool.getMethodrefClassName(poolIndex)
                            val name = pool.getMethodrefName(poolIndex)
                            val desc = pool.getMethodrefType(poolIndex)
                            MethodInsn(method, index, opcode, parent, name, desc)
                        }
                    }
                    InsnUtil.isInvokeDynamicInsn(opcode) -> {
                        poolIndex = itr.u16bitAt(poolIndex)
                        val type = pool.getInvokeDynamicType(poolIndex)
                        val nameIndex = pool.getInvokeDynamicNameAndType(poolIndex)
                        val name = pool.getUtf8Info(nameIndex) // TODO: check that this is correct.
                        val bootstrap = pool.getInvokeDynamicBootstrap(poolIndex)
                        insn = InvokeDynamicInsn(method, index, opcode, name, type, bootstrap)
                    }
                    InsnUtil.isTypeInsn(opcode) -> {
                        poolIndex = itr.u16bitAt(poolIndex)
                        val type = pool.getClassInfo(poolIndex)
                        insn = if (opcode == MULTIANEWARRAY) {
                            val dims = -1 // handle?
                            MultiANewArrayInsn(method, index, opcode, type, dims)
                        } else {
                            TypeInsn(method, index, opcode, type)
                        }
                    }
                    opcode == WIDE -> {
                        opcode = itr.byteAt(poolIndex)
                        index = itr.u16bitAt(poolIndex + 1)
                        insn = CtInsn(method, index, opcode)
                    }
                    InsnUtil.isTableSwitchInsn(opcode) -> {
                        var key = (index and 3.inv()) + 4
                        val dfltIdx = index + itr.s32bitAt(key)
                        key += 4
                        val low = itr.s32bitAt(key)
                        key += 4
                        val high = itr.s32bitAt(key)
                        key += 4
                        val end = (high - low + 1) * 4 + (key)
                        val size = low + (end - index) / 4
                        val indices = IntArray(size)
                        var idx = low
                        while (index < end) {
                            indices[idx - low] = index + itr.s32bitAt(key)
                            index += 4
                            idx++
                        }
                        insn = TableSwitchInsn(method, index, opcode, low, high, dfltIdx, indices)
                    }
                    InsnUtil.isLookupSwitchInsn(opcode) -> {
                        var key = (index and 3.inv()) + 4
                        val dfltIdx = index + itr.s32bitAt(key)
                        key += 4
                        val size = itr.s32bitAt(key)
                        val keys = IntArray(size)
                        val indices = IntArray(size)
                        key += 4
                        for (i in 0 until size) {
                            val match = itr.s32bitAt(key)
                            val target = index + itr.s32bitAt(key + 4)
                            keys[i] = match
                            indices[i] = target
                            key += 8
                        }
                        insn = LookupSwitchInsn(method, index, opcode, dfltIdx, keys, indices)
                    }
                    else -> insn = CtInsn(method, index, opcode)
                }
                if (previous != null) {
                    previous.next.set(insn)
                    insn.previous.set(previous)
                }
                previous = insn
                method.index(insn.index, instructions.size)
                instructions.add(insn)
            }
            return instructions
        } catch (e: BadBytecode) {
            throw IllegalStateException("Unable to parse instructions for ${method.longName}")
        }
    }
}
