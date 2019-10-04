package io.disassemble.javanalysis

import io.disassemble.javanalysis.insn.CtInsn
import io.disassemble.javanalysis.util.query.InsnQuery

class InsnLine {

    private var _list = ArrayList<CtInsn>()

    val list: List<CtInsn>
        get() = _list

    fun add(insn: CtInsn) {
        _list.add(insn)
    }

    fun find(vararg queries: InsnQuery): Map<String, CtInsn> {
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