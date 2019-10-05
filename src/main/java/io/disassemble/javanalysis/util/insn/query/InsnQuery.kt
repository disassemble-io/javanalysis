package io.disassemble.javanalysis.util.insn.query

import io.disassemble.javanalysis.insn.CtInsn
import javassist.bytecode.Opcode

/**
 * A class representing a query.
 * Used for filtering in lists.
 */
open class InsnQuery<T> where T : CtInsn {

    /**
     * Gives this query a name.
     * Used for putting into a map within [io.disassemble.javanalysis.InsnLine.find].
     */
    var name: String? = null

    /**
     * Sets the distance allowed to be traveled between queries within [io.disassemble.javanalysis.InsnLine.find].
     */
    var dist = 1

    /**
     * The list of conditions that must be met.
     * Used within [InsnQuery.check]
     */
    var conditions = ArrayList<(T) -> Boolean>()

    /**
     * Sets the value of [InsnQuery.name] and returns itself.
     *
     * @param name The value to set [InsnQuery.name] to.
     *
     * @return The same [InsnQuery] that this method was called from.
     */
    fun name(name: String): InsnQuery<T> {
        this.name = name
        return this
    }

    /**
     * Sets the value of [InsnQuery.dist] and returns itself.
     *
     * @param dist The value to set [InsnQuery.dist] to.
     *
     * @return The same [InsnQuery] that this method was called from.
     */
    fun dist(dist: Int): InsnQuery<T> {
        this.dist = dist
        return this
    }

    /**
     * Adds a condition to [InsnQuery.conditions] in which the matching [CtInsn] must be of one of the given opcodes.
     *
     * @param opcodes A list of opcodes to match.
     *
     * @return The same [InsnQuery] that this method was called from.
     */
    fun opcode(vararg opcodes: Int): InsnQuery<T> {
        conditions.add { opcodes.contains(it.opcode) }
        return this
    }

    /**
     * Checks if this [InsnQuery]'s conditions are met by the given [CtInsn].
     *
     * @param insn The instruction to check against.
     *
     * @return true if the [InsnQuery.conditions] were met, otherwise false.
     */
    fun check(insn: CtInsn): Boolean {
        @Suppress("UNCHECKED_CAST")
        val castedInsn = insn as? T ?: return false

        for (condition in conditions) {
            if (!(condition(castedInsn))) {
                return false
            }
        }

        return true
    }
}

/**
 * Creates an [InsnQuery] filter matching the [Opcode.AALOAD] instruction.
 */
val AALOAD: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.AALOAD) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.AASTORE] instruction.
 */
val AASTORE: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.AASTORE) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.ACONST_NULL] instruction.
 */
val ACONST_NULL: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.ACONST_NULL) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.ALOAD] instruction.
 */
val ALOAD: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.ALOAD) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.ALOAD_0] instruction.
 */
val ALOAD_0: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.ALOAD_0) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.ALOAD_1] instruction.
 */
val ALOAD_1: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.ALOAD_1) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.ALOAD_2] instruction.
 */
val ALOAD_2: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.ALOAD_2) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.ALOAD_3] instruction.
 */
val ALOAD_3: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.ALOAD_3) }
/**
 * Creates a [TypeInsnQuery] filter matching the [Opcode.ANEWARRAY] instruction.
 */
val ANEWARRAY: TypeInsnQuery get() = TypeInsnQuery().apply { opcode(Opcode.ANEWARRAY) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.ARETURN] instruction.
 */
val ARETURN: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.ARETURN) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.ARRAYLENGTH] instruction.
 */
val ARRAYLENGTH: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.ARRAYLENGTH) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.ASTORE] instruction.
 */
val ASTORE: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.ASTORE) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.ASTORE_0] instruction.
 */
val ASTORE_0: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.ASTORE_0) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.ASTORE_1] instruction.
 */
val ASTORE_1: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.ASTORE_1) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.ASTORE_2] instruction.
 */
val ASTORE_2: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.ASTORE_2) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.ASTORE_3] instruction.
 */
val ASTORE_3: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.ASTORE_3) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.ATHROW] instruction.
 */
val ATHROW: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.ATHROW) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.BALOAD] instruction.
 */
val BALOAD: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.BALOAD) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.BASTORE] instruction.
 */
val BASTORE: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.BASTORE) }
/**
 * Creates an [IntInsnQuery] filter matching the [Opcode.BIPUSH] instruction.
 */
val BIPUSH: IntInsnQuery get() = IntInsnQuery().apply { opcode(Opcode.BIPUSH) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.CALOAD] instruction.
 */
val CALOAD: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.CALOAD) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.CASTORE] instruction.
 */
val CASTORE: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.CASTORE) }
/**
 * Creates a [TypeInsnQuery] filter matching the [Opcode.CHECKCAST] instruction.
 */
val CHECKCAST: TypeInsnQuery get() = TypeInsnQuery().apply { opcode(Opcode.CHECKCAST) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.D2F] instruction.
 */
val D2F: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.D2F) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.D2I] instruction.
 */
val D2I: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.D2I) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.D2L] instruction.
 */
val D2L: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.D2L) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.DADD] instruction.
 */
val DADD: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.DADD) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.DALOAD] instruction.
 */
val DALOAD: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.DALOAD) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.DASTORE] instruction.
 */
val DASTORE: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.DASTORE) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.DCMPG] instruction.
 */
val DCMPG: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.DCMPG) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.DCMPL] instruction.
 */
val DCMPL: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.DCMPL) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.DCONST_0] instruction.
 */
val DCONST_0: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.DCONST_0) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.DCONST_1] instruction.
 */
val DCONST_1: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.DCONST_1) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.DDIV] instruction.
 */
val DDIV: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.DDIV) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.DLOAD] instruction.
 */
val DLOAD: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.DLOAD) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.DLOAD_0] instruction.
 */
val DLOAD_0: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.DLOAD_0) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.DLOAD_1] instruction.
 */
val DLOAD_1: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.DLOAD_1) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.DLOAD_2] instruction.
 */
val DLOAD_2: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.DLOAD_2) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.DLOAD_3] instruction.
 */
val DLOAD_3: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.DLOAD_3) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.DMUL] instruction.
 */
val DMUL: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.DMUL) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.DNEG] instruction.
 */
val DNEG: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.DNEG) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.DREM] instruction.
 */
val DREM: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.DREM) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.DRETURN] instruction.
 */
val DRETURN: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.DRETURN) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.DSTORE] instruction.
 */
val DSTORE: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.DSTORE) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.DSTORE_0] instruction.
 */
val DSTORE_0: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.DSTORE_0) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.DSTORE_1] instruction.
 */
val DSTORE_1: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.DSTORE_1) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.DSTORE_2] instruction.
 */
val DSTORE_2: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.DSTORE_2) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.DSTORE_3] instruction.
 */
val DSTORE_3: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.DSTORE_3) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.DSUB] instruction.
 */
val DSUB: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.DSUB) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.DUP] instruction.
 */
val DUP: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.DUP) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.DUP2] instruction.
 */
val DUP2: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.DUP2) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.DUP2_X1] instruction.
 */
val DUP2_X1: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.DUP2_X1) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.DUP2_X2] instruction.
 */
val DUP2_X2: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.DUP2_X2) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.DUP_X1] instruction.
 */
val DUP_X1: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.DUP_X1) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.DUP_X2] instruction.
 */
val DUP_X2: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.DUP_X2) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.F2D] instruction.
 */
val F2D: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.F2D) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.F2I] instruction.
 */
val F2I: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.F2I) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.F2L] instruction.
 */
val F2L: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.F2L) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.FADD] instruction.
 */
val FADD: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.FADD) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.FALOAD] instruction.
 */
val FALOAD: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.FALOAD) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.FASTORE] instruction.
 */
val FASTORE: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.FASTORE) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.FCMPG] instruction.
 */
val FCMPG: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.FCMPG) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.FCMPL] instruction.
 */
val FCMPL: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.FCMPL) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.FCONST_0] instruction.
 */
val FCONST_0: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.FCONST_0) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.FCONST_1] instruction.
 */
val FCONST_1: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.FCONST_1) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.FCONST_2] instruction.
 */
val FCONST_2: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.FCONST_2) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.FDIV] instruction.
 */
val FDIV: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.FDIV) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.FLOAD] instruction.
 */
val FLOAD: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.FLOAD) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.FLOAD_0] instruction.
 */
val FLOAD_0: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.FLOAD_0) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.FLOAD_1] instruction.
 */
val FLOAD_1: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.FLOAD_1) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.FLOAD_2] instruction.
 */
val FLOAD_2: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.FLOAD_2) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.FLOAD_3] instruction.
 */
val FLOAD_3: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.FLOAD_3) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.FMUL] instruction.
 */
val FMUL: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.FMUL) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.FNEG] instruction.
 */
val FNEG: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.FNEG) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.FREM] instruction.
 */
val FREM: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.FREM) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.FRETURN] instruction.
 */
val FRETURN: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.FRETURN) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.FSTORE] instruction.
 */
val FSTORE: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.FSTORE) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.FSTORE_0] instruction.
 */
val FSTORE_0: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.FSTORE_0) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.FSTORE_1] instruction.
 */
val FSTORE_1: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.FSTORE_1) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.FSTORE_2] instruction.
 */
val FSTORE_2: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.FSTORE_2) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.FSTORE_3] instruction.
 */
val FSTORE_3: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.FSTORE_3) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.FSUB] instruction.
 */
val FSUB: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.FSUB) }
/**
 * Creates a [FieldInsnQuery] filter matching the [Opcode.GETFIELD] instruction.
 */
val GETFIELD: FieldInsnQuery get() = FieldInsnQuery().apply { opcode(Opcode.GETFIELD) }
/**
 * Creates a [FieldInsnQuery] filter matching the [Opcode.GETSTATIC] instruction.
 */
val GETSTATIC: FieldInsnQuery get() = FieldInsnQuery().apply { opcode(Opcode.GETSTATIC) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.GOTO] instruction.
 */
val GOTO: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.GOTO) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.GOTO_W] instruction.
 */
val GOTO_W: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.GOTO_W) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.I2B] instruction.
 */
val I2B: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.I2B) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.I2C] instruction.
 */
val I2C: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.I2C) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.I2D] instruction.
 */
val I2D: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.I2D) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.I2F] instruction.
 */
val I2F: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.I2F) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.I2L] instruction.
 */
val I2L: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.I2L) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.I2S] instruction.
 */
val I2S: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.I2S) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.IADD] instruction.
 */
val IADD: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.IADD) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.IALOAD] instruction.
 */
val IALOAD: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.IALOAD) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.IAND] instruction.
 */
val IAND: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.IAND) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.IASTORE] instruction.
 */
val IASTORE: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.IASTORE) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.ICONST_0] instruction.
 */
val ICONST_0: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.ICONST_0) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.ICONST_1] instruction.
 */
val ICONST_1: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.ICONST_1) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.ICONST_2] instruction.
 */
val ICONST_2: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.ICONST_2) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.ICONST_3] instruction.
 */
val ICONST_3: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.ICONST_3) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.ICONST_4] instruction.
 */
val ICONST_4: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.ICONST_4) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.ICONST_5] instruction.
 */
val ICONST_5: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.ICONST_5) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.ICONST_M1] instruction.
 */
val ICONST_M1: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.ICONST_M1) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.IDIV] instruction.
 */
val IDIV: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.IDIV) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.IFEQ] instruction.
 */
val IFEQ: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.IFEQ) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.IFGE] instruction.
 */
val IFGE: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.IFGE) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.IFGT] instruction.
 */
val IFGT: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.IFGT) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.IFLE] instruction.
 */
val IFLE: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.IFLE) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.IFLT] instruction.
 */
val IFLT: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.IFLT) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.IFNE] instruction.
 */
val IFNE: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.IFNE) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.IFNONNULL] instruction.
 */
val IFNONNULL: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.IFNONNULL) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.IFNULL] instruction.
 */
val IFNULL: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.IFNULL) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.IF_ACMPEQ] instruction.
 */
val IF_ACMPEQ: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.IF_ACMPEQ) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.IF_ACMPNE] instruction.
 */
val IF_ACMPNE: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.IF_ACMPNE) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.IF_ICMPEQ] instruction.
 */
val IF_ICMPEQ: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.IF_ICMPEQ) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.IF_ICMPGE] instruction.
 */
val IF_ICMPGE: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.IF_ICMPGE) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.IF_ICMPGT] instruction.
 */
val IF_ICMPGT: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.IF_ICMPGT) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.IF_ICMPLE] instruction.
 */
val IF_ICMPLE: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.IF_ICMPLE) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.IF_ICMPLT] instruction.
 */
val IF_ICMPLT: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.IF_ICMPLT) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.IF_ICMPNE] instruction.
 */
val IF_ICMPNE: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.IF_ICMPNE) }
/**
 * Creates an [IncrementInsnQuery] filter matching the [Opcode.IINC] instruction.
 */
val IINC: IncrementInsnQuery get() = IncrementInsnQuery().apply { opcode(Opcode.IINC) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.ILOAD] instruction.
 */
val ILOAD: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.ILOAD) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.ILOAD_0] instruction.
 */
val ILOAD_0: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.ILOAD_0) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.ILOAD_1] instruction.
 */
val ILOAD_1: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.ILOAD_1) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.ILOAD_2] instruction.
 */
val ILOAD_2: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.ILOAD_2) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.ILOAD_3] instruction.
 */
val ILOAD_3: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.ILOAD_3) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.IMUL] instruction.
 */
val IMUL: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.IMUL) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.INEG] instruction.
 */
val INEG: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.INEG) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.INSTANCEOF] instruction.
 */
val INSTANCEOF: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.INSTANCEOF) }
/**
 * Creates an [InvokeDynamicInsnQuery] filter matching the [Opcode.INVOKEDYNAMIC] instruction.
 */
val INVOKEDYNAMIC: InvokeDynamicInsnQuery get() = InvokeDynamicInsnQuery().apply { opcode(Opcode.INVOKEDYNAMIC) }
/**
 * Creates a [MethodInsnQuery] filter matching the [Opcode.INVOKEINTERFACE] instruction.
 */
val INVOKEINTERFACE: MethodInsnQuery get() = MethodInsnQuery().apply { opcode(Opcode.INVOKEINTERFACE) }
/**
 * Creates a [MethodInsnQuery] filter matching the [Opcode.INVOKESPECIAL] instruction.
 */
val INVOKESPECIAL: MethodInsnQuery get() = MethodInsnQuery().apply { opcode(Opcode.INVOKESPECIAL) }
/**
 * Creates a [MethodInsnQuery] filter matching the [Opcode.INVOKESTATIC] instruction.
 */
val INVOKESTATIC: MethodInsnQuery get() = MethodInsnQuery().apply { opcode(Opcode.INVOKESTATIC) }
/**
 * Creates a [MethodInsnQuery] filter matching the [Opcode.INVOKEVIRTUAL] instruction.
 */
val INVOKEVIRTUAL: MethodInsnQuery get() = MethodInsnQuery().apply { opcode(Opcode.INVOKEVIRTUAL) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.IOR] instruction.
 */
val IOR: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.IOR) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.IREM] instruction.
 */
val IREM: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.IREM) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.IRETURN] instruction.
 */
val IRETURN: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.IRETURN) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.ISHL] instruction.
 */
val ISHL: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.ISHL) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.ISHR] instruction.
 */
val ISHR: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.ISHR) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.ISTORE] instruction.
 */
val ISTORE: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.ISTORE) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.ISTORE_0] instruction.
 */
val ISTORE_0: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.ISTORE_0) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.ISTORE_1] instruction.
 */
val ISTORE_1: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.ISTORE_1) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.ISTORE_2] instruction.
 */
val ISTORE_2: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.ISTORE_2) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.ISTORE_3] instruction.
 */
val ISTORE_3: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.ISTORE_3) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.ISUB] instruction.
 */
val ISUB: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.ISUB) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.IUSHR] instruction.
 */
val IUSHR: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.IUSHR) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.IXOR] instruction.
 */
val IXOR: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.IXOR) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.JSR] instruction.
 */
val JSR: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.JSR) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.JSR_W] instruction.
 */
val JSR_W: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.JSR_W) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.L2D] instruction.
 */
val L2D: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.L2D) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.L2F] instruction.
 */
val L2F: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.L2F) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.L2I] instruction.
 */
val L2I: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.L2I) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.LADD] instruction.
 */
val LADD: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.LADD) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.LALOAD] instruction.
 */
val LALOAD: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.LALOAD) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.LAND] instruction.
 */
val LAND: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.LAND) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.LASTORE] instruction.
 */
val LASTORE: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.LASTORE) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.LCMP] instruction.
 */
val LCMP: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.LCMP) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.LCONST_0] instruction.
 */
val LCONST_0: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.LCONST_0) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.LCONST_1] instruction.
 */
val LCONST_1: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.LCONST_1) }
/**
 * Creates an [LdcInsnQuery] filter matching the [Opcode.LDC] instruction.
 */
val LDC: LdcInsnQuery get() = LdcInsnQuery().apply { opcode(Opcode.LDC) }
/**
 * Creates an [LdcInsnQuery] filter matching the [Opcode.LDC2_W] instruction.
 */
val LDC2_W: LdcInsnQuery get() = LdcInsnQuery().apply { opcode(Opcode.LDC2_W) }
/**
 * Creates an [LdcInsnQuery] filter matching the [Opcode.LDC_W] instruction.
 */
val LDC_W: LdcInsnQuery get() = LdcInsnQuery().apply { opcode(Opcode.LDC_W) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.LDIV] instruction.
 */
val LDIV: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.LDIV) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.LLOAD] instruction.
 */
val LLOAD: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.LLOAD) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.LLOAD_0] instruction.
 */
val LLOAD_0: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.LLOAD_0) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.LLOAD_1] instruction.
 */
val LLOAD_1: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.LLOAD_1) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.LLOAD_2] instruction.
 */
val LLOAD_2: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.LLOAD_2) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.LLOAD_3] instruction.
 */
val LLOAD_3: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.LLOAD_3) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.LMUL] instruction.
 */
val LMUL: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.LMUL) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.LNEG] instruction.
 */
val LNEG: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.LNEG) }
/**
 * Creates a [LookupSwitchInsnQuery] filter matching the [Opcode.LOOKUPSWITCH] instruction.
 */
val LOOKUPSWITCH: LookupSwitchInsnQuery get() = LookupSwitchInsnQuery().apply { opcode(Opcode.LOOKUPSWITCH) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.LOR] instruction.
 */
val LOR: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.LOR) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.LREM] instruction.
 */
val LREM: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.LREM) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.LRETURN] instruction.
 */
val LRETURN: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.LRETURN) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.LSHL] instruction.
 */
val LSHL: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.LSHL) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.LSHR] instruction.
 */
val LSHR: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.LSHR) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.LSTORE] instruction.
 */
val LSTORE: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.LSTORE) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.LSTORE_0] instruction.
 */
val LSTORE_0: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.LSTORE_0) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.LSTORE_1] instruction.
 */
val LSTORE_1: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.LSTORE_1) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.LSTORE_2] instruction.
 */
val LSTORE_2: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.LSTORE_2) }
/**
 * Creates a [VarInsnQuery] filter matching the [Opcode.LSTORE_3] instruction.
 */
val LSTORE_3: VarInsnQuery get() = VarInsnQuery().apply { opcode(Opcode.LSTORE_3) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.LSUB] instruction.
 */
val LSUB: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.LSUB) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.LUSHR] instruction.
 */
val LUSHR: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.LUSHR) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.LXOR] instruction.
 */
val LXOR: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.LXOR) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.MONITORENTER] instruction.
 */
val MONITORENTER: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.MONITORENTER) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.MONITOREXIT] instruction.
 */
val MONITOREXIT: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.MONITOREXIT) }
/**
 * Creates a [MultiANewArrayInsnQuery] filter matching the [Opcode.MULTIANEWARRAY] instruction.
 */
val MULTIANEWARRAY: MultiANewArrayInsnQuery get() = MultiANewArrayInsnQuery().apply { opcode(Opcode.MULTIANEWARRAY) }
/**
 * Creates a [TypeInsnQuery] filter matching the [Opcode.NEW] instruction.
 */
val NEW: TypeInsnQuery get() = TypeInsnQuery().apply { opcode(Opcode.NEW) }
/**
 * Creates a [TypeInsnQuery] filter matching the [Opcode.NEWARRAY] instruction.
 */
val NEWARRAY: TypeInsnQuery get() = TypeInsnQuery().apply { opcode(Opcode.NEWARRAY) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.NOP] instruction.
 */
val NOP: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.NOP) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.POP] instruction.
 */
val POP: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.POP) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.POP2] instruction.
 */
val POP2: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.POP2) }
/**
 * Creates a [FieldInsnQuery] filter matching the [Opcode.PUTFIELD] instruction.
 */
val PUTFIELD: FieldInsnQuery get() = FieldInsnQuery().apply { opcode(Opcode.PUTFIELD) }
/**
 * Creates a [FieldInsnQuery] filter matching the [Opcode.PUTSTATIC] instruction.
 */
val PUTSTATIC: FieldInsnQuery get() = FieldInsnQuery().apply { opcode(Opcode.PUTSTATIC) }
/**
 * Creates a [JumpInsnQuery] filter matching the [Opcode.RET] instruction.
 */
val RET: JumpInsnQuery get() = JumpInsnQuery().apply { opcode(Opcode.RET) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.RETURN] instruction.
 */
val RETURN: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.RETURN) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.SALOAD] instruction.
 */
val SALOAD: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.SALOAD) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.SASTORE] instruction.
 */
val SASTORE: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.SASTORE) }
/**
 * Creates an [IntInsnQuery] filter matching the [Opcode.SIPUSH] instruction.
 */
val SIPUSH: IntInsnQuery get() = IntInsnQuery().apply { opcode(Opcode.SIPUSH) }
/**
 * Creates an [InsnQuery] filter matching the [Opcode.SWAP] instruction.
 */
val SWAP: CtInsnQuery get() = CtInsnQuery().apply { opcode(Opcode.SWAP) }
/**
 * Creates a [TableSwitchInsnQuery] filter matching the [Opcode.TABLESWITCH] instruction.
 */
val TABLESWITCH: TableSwitchInsnQuery get() = TableSwitchInsnQuery().apply { opcode(Opcode.TABLESWITCH) }
/**
 * Creates a [WideInsnQuery] filter matching the [Opcode.WIDE] instruction.
 */
val WIDE: WideInsnQuery get() = WideInsnQuery().apply { opcode(Opcode.WIDE) }