package io.disassemble.javanalysis;

import javassist.CtClass;
import javassist.CtMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyler Sedlar
 * @since 5/19/2017
 */
public class CtClassNode {

    private final CtClass source;
    private final List<CtMethodNode> methods = new ArrayList<>();

    public CtClassNode(CtClass source) {
        this.source = source;
        for (CtMethod method : source.getDeclaredMethods()) {
            methods.add(new CtMethodNode(method, this));
        }
    }

    public CtClass source() {
        return source;
    }

    public String name() {
        return source.getName();
    }

    public List<CtMethodNode> methods() {
        return methods;
    }
}
