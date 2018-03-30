package io.disassemble.javanalysis.util

import io.disassemble.javanalysis.insn.*
import javassist.CtMethod
import javassist.bytecode.*
import com.sun.org.apache.bcel.internal.generic.RET
import com.sun.org.apache.bcel.internal.generic.IINC
import com.sun.org.apache.bcel.internal.generic.ASTORE
import com.sun.org.apache.bcel.internal.generic.DSTORE
import com.sun.org.apache.bcel.internal.generic.FSTORE
import com.sun.org.apache.bcel.internal.generic.LSTORE
import com.sun.org.apache.bcel.internal.generic.ISTORE
import com.sun.org.apache.bcel.internal.generic.ALOAD
import com.sun.org.apache.bcel.internal.generic.DLOAD
import com.sun.org.apache.bcel.internal.generic.FLOAD
import com.sun.org.apache.bcel.internal.generic.LLOAD
import com.sun.org.apache.bcel.internal.generic.ILOAD
import javassist.bytecode.CodeIterator



/**
 * Simple utility class for parsing the bytecode instructions of a method.
 *
 * @author Tyler Sedlar
 * @since 3/30/2018
 */
object CodeParser : Opcode {

    private val opcodes = Mnemonic.OPCODE

    /**
     * Parses the bytecode instructions of a given method.
     */
    fun parse(method: CtMethod): List<CtInsn> {
        val insns: MutableList<CtInsn> = ArrayList()
        val info = method.methodInfo2
        val pool = info.constPool
        val code = info.codeAttribute ?: return insns
        val iterator = code.iterator()
        while (iterator.hasNext()) {
            val pos: Int
            try {
                pos = iterator.next()
            } catch (e: BadBytecode) {
                throw RuntimeException(e)
            }
            insns.add(parse(method, iterator, pos, pool))
        }
        return insns
    }

    /**
     * Gets a string representation of the bytecode instruction at the specified
     * position.
     */
    fun parse(method: CtMethod, iter: CodeIterator, pos: Int, pool: ConstPool): CtInsn {
        val opcode = iter.byteAt(pos)

        if (opcode > opcodes.size || opcode < 0)
            throw IllegalArgumentException("Invalid opcode, opcode: $opcode pos: $pos")

        when (opcode) {
            Opcode.BIPUSH -> {
                return IntInsn(method, pos, opcode, iter.byteAt(pos + 1))
            }
            Opcode.SIPUSH -> {
                return IntInsn(method, pos, opcode, iter.s16bitAt(pos + 1))
            }
            Opcode.LDC -> {
                return LdcInsn(method, pos, opcode, iter.byteAt(pos + 1))
            }
            Opcode.LDC_W, Opcode.LDC2_W -> {
                return LdcInsn(method, pos, opcode, iter.u16bitAt(pos + 1))
            }
            Opcode.ILOAD, Opcode.LLOAD, Opcode.FLOAD, Opcode.DLOAD, Opcode.ALOAD,
            Opcode.ISTORE, Opcode.LSTORE, Opcode.FSTORE, Opcode.DSTORE, Opcode.ASTORE -> {
                return VarInsn(method, pos, opcode, iter.byteAt(pos + 1))
            }
            Opcode.IFEQ, Opcode.IFGE, Opcode.IFGT, Opcode.IFLE, Opcode.IFLT, Opcode.IFNE,
            Opcode.IFNONNULL, Opcode.IFNULL, Opcode.IF_ACMPEQ, Opcode.IF_ACMPNE, Opcode.IF_ICMPEQ,
            Opcode.IF_ICMPGE, Opcode.IF_ICMPGT, Opcode.IF_ICMPLE, Opcode.IF_ICMPLT, Opcode.IF_ICMPNE -> {
                return JumpInsn(method, pos, opcode, (iter.s16bitAt(pos + 1) + pos))
            }
            Opcode.IINC -> {
                return IncrementInsn(method, pos, opcode, iter.byteAt(pos + 1),
                        iter.signedByteAt(pos + 2))
            }
            Opcode.GOTO, Opcode.JSR -> {
                return JumpInsn(method, pos, opcode, iter.s16bitAt(pos + 1) + pos)
            }
            Opcode.RET -> {
                return JumpInsn(method, pos, opcode, iter.byteAt(pos + 1))
            }
            Opcode.TABLESWITCH -> {
                return tableSwitch(method, opcode, iter, pos)
            }
            Opcode.LOOKUPSWITCH -> {
                return lookupSwitch(method, opcode, iter, pos)
            }
            Opcode.GETSTATIC, Opcode.PUTSTATIC, Opcode.GETFIELD, Opcode.PUTFIELD -> {
                return fieldInfo(method, pos, opcode, pool, iter.u16bitAt(pos + 1))
            }
            Opcode.INVOKEVIRTUAL, Opcode.INVOKESPECIAL, Opcode.INVOKESTATIC -> {
                return methodInfo(method, pos, opcode, pool, iter.u16bitAt(pos + 1))
            }
            Opcode.INVOKEINTERFACE -> {
                return interfaceMethodInfo(method, pos, opcode, pool, iter.u16bitAt(pos + 1))
            }
            Opcode.INVOKEDYNAMIC -> {
                val poolIndex = iter.u16bitAt(pos + 1)
                val type = pool.getInvokeDynamicType(poolIndex)
                val nameAndType = pool.getInvokeDynamicNameAndType(poolIndex)
                val bootstrap = pool.getInvokeDynamicBootstrap(poolIndex)
                return InvokeDynamicInsn(method, pos, opcode, nameAndType, type, bootstrap)
            }
            Opcode.NEW -> {
                return classInfo(method, pos, opcode, pool, iter.u16bitAt(pos + 1))
            }
            Opcode.NEWARRAY -> {
                return TypeInsn(method, pos, opcode, arrayInfo(iter.byteAt(pos + 1)))
            }
            Opcode.ANEWARRAY, Opcode.CHECKCAST -> {
                return classInfo(method, pos, opcode, pool, iter.u16bitAt(pos + 1))
            }
            Opcode.WIDE -> {
                return wide(method, pos, opcode, iter)
            }
            Opcode.MULTIANEWARRAY -> {
                return classInfo(method, pos, opcode, pool, iter.u16bitAt(pos + 1))
            }
            Opcode.GOTO_W, Opcode.JSR_W -> {
                return JumpInsn(method, pos, opcode, iter.s32bitAt(pos + 1) + pos)
            }
            else -> {
                return CtInsn(method, pos, opcode)
            }
        }
    }

    private fun arrayInfo(type: Int): String {
        return when (type) {
            Opcode.T_BOOLEAN -> "boolean"
            Opcode.T_CHAR -> "char"
            Opcode.T_BYTE -> "byte"
            Opcode.T_SHORT -> "short"
            Opcode.T_INT -> "int"
            Opcode.T_LONG -> "long"
            Opcode.T_FLOAT -> "float"
            Opcode.T_DOUBLE -> "double"
            else -> throw RuntimeException("Invalid array type")
        }
    }

    private fun wide(method: CtMethod, pos: Int, opcode: Int, iter: CodeIterator): CtInsn {
        val wideOpcode = iter.byteAt(pos + 1)
        val index = iter.u16bitAt(pos + 2)
        return WideInsn(method, pos, opcode, wideOpcode, index)
    }


    private fun classInfo(method: CtMethod, pos: Int, opcode: Int, pool: ConstPool, index: Int): TypeInsn {
        return TypeInsn(method, pos, opcode, pool.getClassInfo(index))
    }

    private fun interfaceMethodInfo(method: CtMethod, pos: Int, opcode: Int, pool: ConstPool, index: Int): MethodInsn {
        return MethodInsn(method, pos, opcode, pool.getInterfaceMethodrefClassName(index),
                pool.getInterfaceMethodrefName(index), pool.getInterfaceMethodrefType(index))
    }

    private fun methodInfo(method: CtMethod, pos: Int, opcode: Int, pool: ConstPool, index: Int): MethodInsn {
        return MethodInsn(method, pos, opcode, pool.getMethodrefClassName(index),
                pool.getMethodrefName(index), pool.getMethodrefType(index))
    }

    private fun fieldInfo(method: CtMethod, pos: Int, opcode: Int, pool: ConstPool, index: Int): FieldInsn {
        return FieldInsn(method, pos, opcode, pool.getFieldrefClassName(index),
                pool.getFieldrefName(index), pool.getFieldrefType(index))
    }

    private fun lookupSwitch(method: CtMethod, opcode: Int, iter: CodeIterator, pos: Int): LookupSwitchInsn {
        var index = (pos and 3.inv()) + 4
        val defaultIndex = pos + iter.s32bitAt(index)

        val npairs = iter.s32bitAt(index + 4)
        index += 4

        val end = npairs * 8 + (index + 4)
        index += 4

        val keys: MutableList<Int> = ArrayList()
        val indices: MutableList<Int> = ArrayList()

        while (index < end) {
            keys.add(iter.s32bitAt(index))
            indices.add(iter.s32bitAt(index + 4) + pos)
            index += 8
        }

        return LookupSwitchInsn(method, pos, opcode, defaultIndex, keys.toIntArray(), indices.toIntArray())
    }

    private fun tableSwitch(method: CtMethod, opcode: Int, iter: CodeIterator, pos: Int): TableSwitchInsn {
        var index = (pos and 3.inv()) + 4
        val defaultIndex = pos + iter.s32bitAt(index)

        val low = iter.s32bitAt(index + 4)
        index += 4

        val high = iter.s32bitAt(index + 4)
        index += 4

        val end = (high - low + 1) * 4 + (index + 4)
        index += 4


        val indices: MutableList<Int> = ArrayList()

        // Offset table
        var key = low
        while (index < end) {
            indices.add(iter.s32bitAt(index) + pos)
            index += 4
            key++
        }

        return TableSwitchInsn(method, pos, opcode, low, high, defaultIndex, indices.toIntArray())
    }
}
