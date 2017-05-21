package io.disassemble.javanalysis.util;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Tyler Sedlar
 * @since 5/19/2017
 */
public class CtClassScanner {

    public static void scanClassPath(Predicate<CtClass> predicate, Consumer<CtMethod> consumer) {
        String list = System.getProperty("java.class.path");
        for (String path : list.split(File.pathSeparator)) {
            File file = new File(path);
            if (file.isDirectory()) {
                scanDirectory(file, predicate, consumer);
            } else if (path.endsWith(".class")) {
                scanClassFile(path, predicate, consumer);
            }
        }
    }

    public static void scanDirectory(File directory, Predicate<CtClass> predicate, Consumer<CtMethod> consumer) {
        for (String entry : directory.list()) {
            String path = directory.getPath() + File.separator + entry;
            File file = new File(path);
            if (file.isDirectory()) {
                scanDirectory(file, predicate, consumer);
            } else if (file.isFile() && path.endsWith(".class")) {
                scanClassFile(path, predicate, consumer);
            }
        }
    }

    public static void scanClassFile(String path, Predicate<CtClass> predicate, Consumer<CtMethod> consumer) {
        try (InputStream input = new FileInputStream(path)) {
            scanInputStream(input, predicate, consumer);
        } catch (IOException e) {
            System.out.println("File was not found: " + path);
        }
    }

    public static void scanInputStream(InputStream is, Predicate<CtClass> predicate, Consumer<CtMethod> consumer) {
        try {
            ClassPool pool = new ClassPool();
            pool.appendSystemPath();
            CtClass node = pool.makeClass(is);
            if (!predicate.test(node)) {
                return;
            }
            for (CtMethod method : node.getDeclaredMethods()) {
                consumer.accept(method);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, CtClass> scanJar(File file) {
        Map<String, CtClass> classes = new HashMap<>();
        Predicate<CtClass> predicate = (cn) -> true;
        Consumer<CtMethod> consumer = (me) -> {
            CtClass declared = me.getDeclaringClass();
            classes.put(declared.getName(), declared);
        };
        try (JarFile jar = new JarFile(file)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    scanInputStream(jar.getInputStream(entry), predicate, consumer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }
}