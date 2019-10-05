package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */

/**
 * A class representing a lookupswitch instruction.
 *
 * @param owner The [CtMethod] that this instruction is a part of.
 * @param index The index of this instruction in [CtMethod].
 */
class LookupSwitchInsn(
        owner: CtMethod,
        index: Int
) : CtInsn(owner, index) {

    private val _keys = ArrayList<Int>()
    private val _indices = ArrayList<Int>()

    /**
     * The keys of this [LookupSwitchInsn].
     */
    val keys: IntArray // immutable
        get() = _keys.toIntArray()

    /**
     * The indices being looked up.
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
     * The number of pairs within this [LookupSwitchInsn].
     */
    val npairs: Int
        get() = owner.code.iterator().s32bitAt(start + 4)

    /**
     * The end index to internally look at within [javassist.bytecode.CodeIterator].
     */
    val end: Int
        get() = npairs * 8 + (start + 8)

    init {
        populateKeysAndIndices()
    }

    /**
     * Populates both [keys] and [indices].
     *
     * @param force True to clear [keys] and [indices] to be force-filled, otherwise false.
     */
    fun populateKeysAndIndices(force: Boolean = false) {
        if (force) {
            _keys.clear()
            _indices.clear()
        }
        if (_keys.isEmpty() && _indices.isEmpty()) {
            var idx = start + 8
            val iter = owner.code.iterator()
            while (idx < end) {
                _keys.add(iter.s32bitAt(idx))
                _indices.add(iter.s32bitAt(idx + 4) + index)
                idx += 8
            }
        }
    }
}
