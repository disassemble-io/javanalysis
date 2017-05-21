import io.disassemble.javanalysis.CtClassNode;
import io.disassemble.javanalysis.flow.ControlFlowGraph;
import io.disassemble.javanalysis.util.CtClassScanner;
import io.disassemble.javanalysis.util.InsnUtil;
import javassist.CtClass;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tyler Sedlar
 * @since 5/18/2017
 */
public class Javassist {

    private static final String TEST_CLASS_NAME = "samples.Sample";
    private static final String TEST_CLASSES = "./target/test-classes/";

    private static boolean local = true;

    private static Map<String, CtClassNode> classes;

    @BeforeClass
    public static void setup() {
        if (local) {
            classes = new HashMap<>();
            CtClassScanner.scanClassPath(
                    cn -> cn.getName().equals(TEST_CLASS_NAME),
                    cm -> {
                        CtClass declared = cm.getDeclaringClass();
                        classes.put(declared.getName(), new CtClassNode(declared));
                    }
            );
        }
    }

    @Test
    public void debug() {
        CtClassNode clazz = classes.get(TEST_CLASS_NAME);
        clazz.methods().forEach(method -> {
            ControlFlowGraph cfg = method.cfg();
            System.out.println(method.key());
            cfg.blocks().forEach(block -> {
                System.out.printf(
                        "FlowBlock (range = %s-%s, exits = %s, incoming = %s) {\n",
                        block.startIndex(), block.endIndex(),
                        Arrays.toString(block.exitIndices()),
                        Arrays.toString(block.incomingIndices())
                );
                block.instructions().forEach(insn -> {
                    System.out.println("  " + InsnUtil.stringify(insn));
                });
                System.out.println("}");
            });
            System.out.println();
        });
    }

    @After
    public void finish() {
        if (local) {
            System.out.println("Finished.");
        }
    }
}
