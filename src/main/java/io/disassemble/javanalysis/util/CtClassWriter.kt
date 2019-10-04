package io.disassemble.javanalysis.util

import javassist.CtClass
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream

fun Map<String, CtClass>.write(outFile: File) {
    JarOutputStream(FileOutputStream(outFile)).use { jar ->
        this.forEach { (className, classEntry) ->
            val entry = JarEntry("$className.class")
            jar.putNextEntry(entry)
            jar.write(classEntry.toBytecode())
            jar.closeEntry()
        }
        jar.close()
    }
}