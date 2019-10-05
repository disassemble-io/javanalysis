package io.disassemble.javanalysis

import io.disassemble.javanalysis.insn.CtInsn
import io.disassemble.javanalysis.util.insn.query.InsnQuery

/**
 * @author Tyler Sedlar
 */

/**
 * A structure representing a line of code.
 * Acts as a [List] of [CtInsn].
 *
 * @param line The line that the accompanied [CtInsn] appear on.
 */
class InsnLine(val line: Int) {

    /**
     * An internal [List] containing the [CtInsn] on this line.
     */
    private var _list = ArrayList<CtInsn>()

    /**
     * The [CtInsn] that appear on this line.
     */
    val list: List<CtInsn>
        get() = _list

    /**
     * Adds the given [CtInsn] to this line.
     *
     * @param insn The [CtInsn] to add to this line.
     */
    fun add(insn: CtInsn) {
        _list.add(insn)
    }

    /**
     * Searches for the given queries within [list].
     *
     * @param queries The list of [InsnQuery] to search for.
     *
     * @return A map of matching [CtInsn], where the key is [InsnQuery.name].
     */
    fun find(vararg queries: InsnQuery<out CtInsn>): Map<String, CtInsn> {
        val matches = HashMap<String, CtInsn>()

        var idx = 0
        var traveled = 0
        var success = false
        for (insn in list) {
            if (traveled > queries[idx].dist) {
                idx = 0
                traveled = 0
                matches.clear()
            }

            if (queries[idx].check(insn)) {
                queries[idx].name?.let { matches[it] = insn }
                idx++
                traveled = 0
                if (idx >= queries.size) {
                    success = true
                    break
                }
            }

            traveled++
        }

        return if (success) matches else emptyMap()
    }
}