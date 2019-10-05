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
 * A utility class used for parsing a class structure into a [CtClass] structure.
 *
 * @author Tyler Sedlar
 * @since 5/19/2017
 */
object CtClassScanner {

    /**
     * Scans the class path for matching [CtClass].
     *
     * @param predicate A filter for which [CtClass] should have the given [Consumer] ran on it.
     * @param consumer The consumer to run on each of the [CtMethod] within the matching [CtClass].
     */
    fun scanClassPath(predicate: Predicate<CtClass>?, consumer: Consumer<CtMethod>?) {
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

    /**
     * Scans the given directory for matching [CtClass].
     *
     * @param directory The [File] directory to look within.
     * @param predicate A filter for which [CtClass] should have the given [Consumer] ran on it.
     * @param consumer The consumer to run on each of the [CtMethod] within the matching [CtClass].
     */
    fun scanDirectory(directory: File, predicate: Predicate<CtClass>?, consumer: Consumer<CtMethod>?) {
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

    /**
     * Scans the given directory for matching [CtClass], putting it within a map.
     * The map key matches [CtClass.getName]
     *
     * @param directory The [File] directory to look within.
     * @return A map of [CtClass] within the [File] directory, where the key is [CtClass.getName].
     **/
    fun scanDirectory(directory: File): Map<String, CtClass> {
        val classes = HashMap<String, CtClass>()
        val consumer = Consumer<CtMethod> { method ->
            val declared = method.declaringClass
            classes[declared.name] = declared
        }
        scanDirectory(directory, null, consumer)
        return classes
    }

    /**
     * Applies the given [Predicate] and [Consumer] against the class file.
     *
     * @param path The path of the .class file to act upon.
     * @param predicate A filter for which [CtClass] should have the given [Consumer] ran on it.
     * @param consumer The consumer to run on each of the [CtMethod] within the matching [CtClass].
     */
    fun scanClassFile(path: String, predicate: Predicate<CtClass>?, consumer: Consumer<CtMethod>?) {
        try {
            FileInputStream(path).use { input -> scanInputStream(input, predicate, consumer) }
        } catch (e: IOException) {
            println("File was not found: $path")
        }
    }

    /**
     * Reads the given .class file path into a [CtClass] object.
     *
     * @param path The path of the .class file to read.
     *
     * @return A [CtClass] read from the given .class file.
     */
    fun scanClassFile(path: String): CtClass? {
        var ctc: CtClass? = null
        val predicate = Predicate<CtClass> {
            ctc = it
            true
        }
        scanClassFile(path, predicate, null)
        return ctc
    }

    /**
     * Reads the [InputStream] into a [CtClass] object.
     *
     * @param inputStream An [InputStream] representing a .class file.
     * @param predicate A filter for which [CtClass] should have the given [Consumer] ran on it.
     * @param consumer The consumer to run on each of the [CtMethod] within the matching [CtClass].
     */
    fun scanInputStream(inputStream: InputStream, predicate: Predicate<CtClass>?, consumer: Consumer<CtMethod>?) {
        try {
            val pool = ClassPool()
            pool.appendSystemPath()
            val node = pool.makeClass(inputStream)
            if (predicate != null && !predicate.test(node)) {
                return
            }
            if (consumer != null) {
                for (method in node.declaredMethods) {
                    consumer.accept(method)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Reads the given jar [File] path into a [Map] of [CtClass], where the key is [CtClass.getName].
     *
     * @param file The path of the .jar file to read.
     *
     * @return A [Map] of [CtClass], where the key is [CtClass.getName].
     */
    fun scanJar(file: File): Map<String, CtClass> {
        val classes = HashMap<String, CtClass>()
        val predicate = Predicate<CtClass> { true }
        val consumer = Consumer<CtMethod> { method ->
            val declared = method.declaringClass
            classes[declared.name] = declared
        }
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