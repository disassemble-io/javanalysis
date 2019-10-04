package io.disassemble.javanalysis.insn

import io.disassemble.javanalysis.code
import javassist.CtMethod

/**
 * @author Tyler Sedlar
 * @since 5/20/2017
 */
class LookupSwitchInsn(
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

    val npairs: Int
        get() = owner.code.iterator().s32bitAt(start + 4)

    val end: Int
        get() = npairs * 8 + (start + 8)

    init {
        populateKeysAndIndices()
    }

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
