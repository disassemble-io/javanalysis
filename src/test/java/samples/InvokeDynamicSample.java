package samples;

import java.lang.invoke.*;

public class InvokeDynamicSample {

    private static MethodHandle sayHello;

    private static void sayHello() {
        System.out.println("There we go!");
    }

    public static CallSite bootstrapDynamic(MethodHandles.Lookup caller, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        Class thisClass = lookup.lookupClass();
        sayHello = lookup.findStatic(thisClass, "sayHello", MethodType.methodType(void.class));
        return new ConstantCallSite(sayHello.asType(type));
    }
}