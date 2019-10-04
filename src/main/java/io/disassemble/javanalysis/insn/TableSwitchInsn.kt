package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import javassist.CtMethod


/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class TableSwitchInsn(
        owner: CtMethod,
        index: Int
) : CtInsn(owner, index) {

    private val _keys = ArrayList<Int>()
    private val _indices = ArrayList<Int>()

    val keys: IntArray // immutable
        get() = _keys.toIntArray()

    val indices: IntArray // immutable
        get() = _indices.toIntArray()

    val start: Int
        get() = (index and 3.inv()) + 4

    val defaultIndex: Int
        get() = index + owner.code.iterator().s32bitAt(start)

    val low: Int
        get() = owner.code.iterator().s32bitAt(start + 4)

    val high: Int
        get() = owner.code.iterator().s32bitAt(start + 8)

    val end: Int
        get() = (high - low + 1) * 4 + (start + 12)

    init {
        populateIndices()
    }

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