package io.disassemble.javanalysis

import io.disassemble.javanalysis.insn.CtInsn

/**
 * Gets a [Map] of [InsnLine], where the key is the line it appeared on.
 *
 * @return A [Map] of [InsnLine], where the key is the line it appeared on.
 */
fun List<CtInsn>.groupByLines(): Map<Int, InsnLine> {
    val lineMap = HashMap<Int, InsnLine>()
    this.forEach { insn ->
        val line = insn.line
        if (line !in lineMap) {
            lineMap[line] = InsnLine(line)
        }
        lineMap[line]?.add(insn)
    }
    return lineMap
}