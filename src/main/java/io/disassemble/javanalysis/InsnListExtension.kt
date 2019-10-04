package io.disassemble.javanalysis

import io.disassemble.javanalysis.insn.CtInsn

fun List<CtInsn>.groupByLines(): Map<Int, InsnLine> {
    val lineMap = HashMap<Int, InsnLine>()
    this.forEach { insn ->
        val line = insn.line
        if (line !in lineMap) {
            lineMap[line] = InsnLine()
        }
        lineMap[line]?.add(insn)
    }
    return lineMap
}