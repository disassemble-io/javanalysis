package io.disassemble.javanalysis.util

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.jar.JarFile

/**
 * @author Tyler Sedlar
 * @since 5/19/2017
 */
object CtClassScanner {

    fun scanClassPath(predicate: Predicate<CtClass>, consumer: Consumer<CtMethod>) {
        val list = System.getProperty("java.class.path")
        for (path in list.split(File.pathSeparator.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            val file = File(path)
            if (file.isDirectory) {
                scanDirectory(file, predicate, consumer)
            } else if (path.endsWith(".class")) {
                scanClassFile(path, predicate, consumer)
            }
        }
    }

    fun scanDirectory(directory: File, predicate: Predicate<CtClass>, consumer: Consumer<CtMethod>) {
        for (entry in directory.list()!!) {
            val path = "${directory.path}${File.separator}$entry"
            val file = File(path)
            if (file.isDirectory) {
                scanDirectory(file, predicate, consumer)
            } else if (file.isFile && path.endsWith(".class")) {
                scanClassFile(path, predicate, consumer)
            }
        }
    }

    fun scanClassFile(path: String, predicate: Predicate<CtClass>, consumer: Consumer<CtMethod>) {
        try {
            FileInputStream(path).use { input -> scanInputStream(input, predicate, consumer) }
        } catch (e: IOException) {
            println("File was not found: " + path)
        }

    }

    fun scanInputStream(inputStream: InputStream, predicate: Predicate<CtClass>, consumer: Consumer<CtMethod>) {
        try {
            val pool = ClassPool()
            pool.appendSystemPath()
            val node = pool.makeClass(inputStream)
            if (!predicate.test(node)) {
                return
            }
            for (method in node.declaredMethods) {
                consumer.accept(method)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun scanJar(file: File): Map<String, CtClass> {
        val classes = HashMap<String, CtClass>()
        val predicate = Predicate<CtClass>({ true })
        val consumer = Consumer<CtMethod>({ me ->
            val declared = me.declaringClass
            classes[declared.name] = declared
        })
        try {
            JarFile(file).use { jar ->
                val entries = jar.entries()
                while (entries.hasMoreElements()) {
                    val entry = entries.nextElement()
                    if (entry.name.endsWith(".class")) {
                        scanInputStream(jar.getInputStream(entry), predicate, consumer)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return classes
    }
}