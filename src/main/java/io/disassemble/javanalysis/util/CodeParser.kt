package io.disassemble.javanalysis.util

import io.disassemble.javanalysis.code
import io.disassemble.javanalysis.insn.*
import javassist.CtMethod
import javassist.bytecode.BadBytecode
import javassist.bytecode.CodeIterator
import javassist.bytecode.Mnemonic
import javassist.bytecode.Opcode


/**
 * A simple utility class for parsing the bytecode instructions of a method.
 *
 * @author Tyler Sedlar
 * @since 3/30/2018
 */
object CodeParser : Opcode {

    private val opcodes = Mnemonic.OPCODE

    /**
     * Parses the bytecode instructions of the given [CtMethod].
     *
     * @param method The [CtMethod] to parse.
     *
     * @return A list of [CtInsn] contained within the given [CtMethod].
     */
    fun parse(method: CtMethod): List<CtInsn> {
        val insns: MutableList<CtInsn> = ArrayList()
        val info = method.methodInfo2
        info.codeAttribute ?: return insns // early return if no code attribute

        val iterator = method.code.iterator()

        var prevInsn: CtInsn? = null
        while (iterator.hasNext()) {
            val pos: Int
            try {
                pos = iterator.next()
            } catch (e: BadBytecode) {
                throw RuntimeException(e)
            }
            val insn = parse(method, iterator, pos)
            if (prevInsn != null) {
                prevInsn.next = insn
                insn.previous = prevInsn
            }
            prevInsn = insn
            insns.add(insn)
        }
        return insns
    }

    /**
     * Gets a string representation of the bytecode instruction at the specified
     * position.
     *
     * @param method The [CtMethod] to parse from.
     * @param iter The [CtMethod]'s [CodeIterator].
     * @param pos The position of the [CtInsn] to parse.
     *
     * @return The [CtInsn] at the given position.
     */
    fun parse(method: CtMethod, iter: CodeIterator, pos: Int): CtInsn {
        val opcode = iter.byteAt(pos)

        require(!(opcode > opcodes.size || opcode < 0)) { "Invalid opcode, opcode: $opcode pos: $pos" }

        when (opcode) {
            Opcode.BIPUSH, Opcode.SIPUSH -> {
                return IntInsn(method, pos)
            }
            Opcode.LDC, Opcode.LDC_W, Opcode.LDC2_W -> {
                return LdcInsn(method, pos)
            }
            Opcode.ILOAD, Opcode.ILOAD_0, Opcode.ILOAD_1, Opcode.ILOAD_2, Opcode.ILOAD_3,
            Opcode.LLOAD, Opcode.LLOAD_0, Opcode.LLOAD_1, Opcode.LLOAD_2, Opcode.LLOAD_3,
            Opcode.FLOAD, Opcode.FLOAD_0, Opcode.FLOAD_1, Opcode.FLOAD_2, Opcode.FLOAD_3,
            Opcode.DLOAD, Opcode.DLOAD_0, Opcode.DLOAD_1, Opcode.DLOAD_2, Opcode.DLOAD_3,
            Opcode.ALOAD, Opcode.ALOAD_0, Opcode.ALOAD_1, Opcode.ALOAD_2, Opcode.ALOAD_3,
            Opcode.ISTORE, Opcode.ISTORE_0, Opcode.ISTORE_1, Opcode.ISTORE_2, Opcode.ISTORE_3,
            Opcode.LSTORE, Opcode.LSTORE_0, Opcode.LSTORE_1, Opcode.LSTORE_2, Opcode.LSTORE_3,
            Opcode.FSTORE, Opcode.FSTORE_0, Opcode.FSTORE_1, Opcode.FSTORE_2, Opcode.FSTORE_3,
            Opcode.DSTORE, Opcode.DSTORE_0, Opcode.DSTORE_1, Opcode.DSTORE_2, Opcode.DSTORE_3,
            Opcode.ASTORE, Opcode.ASTORE_0, Opcode.ASTORE_1, Opcode.ASTORE_2, Opcode.ASTORE_3 -> {
                return VarInsn(method, pos)
            }
            Opcode.IFEQ, Opcode.IFGE, Opcode.IFGT, Opcode.IFLE, Opcode.IFLT, Opcode.IFNE,
            Opcode.IFNONNULL, Opcode.IFNULL, Opcode.IF_ACMPEQ, Opcode.IF_ACMPNE, Opcode.IF_ICMPEQ,
            Opcode.IF_ICMPGE, Opcode.IF_ICMPGT, Opcode.IF_ICMPLE, Opcode.IF_ICMPLT, Opcode.IF_ICMPNE,
            Opcode.GOTO, Opcode.JSR, Opcode.RET, Opcode.GOTO_W, Opcode.JSR_W -> {
                return JumpInsn(method, pos)
            }
            Opcode.IINC -> {
                return IncrementInsn(method, pos)
            }
            Opcode.TABLESWITCH -> {
                return TableSwitchInsn(method, pos)
            }
            Opcode.LOOKUPSWITCH -> {
                return LookupSwitchInsn(method, pos)
            }
            Opcode.GETSTATIC, Opcode.PUTSTATIC, Opcode.GETFIELD, Opcode.PUTFIELD -> {
                return FieldInsn(method, pos)
            }
            Opcode.INVOKEVIRTUAL, Opcode.INVOKESPECIAL, Opcode.INVOKESTATIC, Opcode.INVOKEINTERFACE -> {
                return MethodInsn(method, pos)
            }
            Opcode.INVOKEDYNAMIC -> {
                return InvokeDynamicInsn(method, pos)
            }
            Opcode.NEW, Opcode.NEWARRAY, Opcode.ANEWARRAY, Opcode.CHECKCAST -> {
                return TypeInsn(method, pos)
            }
            Opcode.MULTIANEWARRAY -> {
                return MultiANewArrayInsn(method, pos)
            }
            Opcode.WIDE -> {
                return WideInsn(method, pos)
            }
            else -> {
                return CtInsn(method, pos)
            }
        }
    }
}
