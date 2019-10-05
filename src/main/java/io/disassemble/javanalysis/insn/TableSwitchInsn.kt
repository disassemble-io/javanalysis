package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import javassist.CtMethod


/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */

/**
 * A class representing a tableswitch instruction.
 *
 * @param owner The [CtMethod] that this instruction is a part of.
 * @param index The index of this instruction in [CtMethod].
 */
class TableSwitchInsn(
        owner: CtMethod,
        index: Int
) : CtInsn(owner, index) {

    private val _keys = ArrayList<Int>()
    private val _indices = ArrayList<Int>()

    /**
     * The keys of this [TableSwitchInsn].
     */
    val keys: IntArray // immutable
        get() = _keys.toIntArray()

    /**
     * The indices within this [TableSwitchInsn].
     */
    val indices: IntArray // immutable
        get() = _indices.toIntArray()

    /**
     * The start index to internally look at within [javassist.bytecode.CodeIterator].
     */
    val start: Int
        get() = (index and 3.inv()) + 4

    /**
     * The default index to internally look at within [javassist.bytecode.CodeIterator].
     */
    val defaultIndex: Int
        get() = index + owner.code.iterator().s32bitAt(start)

    /**
     * The lowest value within the [TableSwitchInsn].
     */
    val low: Int
        get() = owner.code.iterator().s32bitAt(start + 4)

    /**
     * The highest value within the [TableSwitchInsn].
     */
    val high: Int
        get() = owner.code.iterator().s32bitAt(start + 8)

    /**
     * The end index to internally look at within [javassist.bytecode.CodeIterator].
     */
    val end: Int
        get() = (high - low + 1) * 4 + (start + 12)

    init {
        populateIndices()
    }

    /**
     * Populates both [keys] and [indices].
     *
     * @param force True to clear [keys] and [indices] to be force-filled, otherwise false.
     */
    fun populateIndices(force: Boolean = false) {
        if (force) {
            _indices.clear()
        }

        var idx = start + 12
        var key = low

        val iter = owner.code.iterator()

        while (idx < end) {
            _keys.add(key)
            _indices.add(iter.s32bitAt(idx) + index)
            idx += 4
            key++
        }
    }
}