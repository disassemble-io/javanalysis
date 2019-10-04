import io.disassemble.javanalysis.allMethods
import io.disassemble.javanalysis.insn.*
import io.disassemble.javanalysis.instructions
import io.disassemble.javanalysis.owner
import io.disassemble.javanalysis.util.CtClassScanner
import javassist.CtClass
import javassist.CtMethod
import junit.framework.Assert.assertNotNull
import org.junit.Test
import java.io.File
import java.nio.file.Files
import kotlin.test.assertEquals

class WriteTest {

    @Test
    fun iincChangeIncrement() {
        val getInc: (CtMethod) -> IncrementInsn = { ctm ->
            val insn = ctm.instructions.findLast { it is IncrementInsn }
            assertNotNull("IncrementInsn not found", insn)
            insn as IncrementInsn
        }

        invokeSampleMethod("samples.IincKt", "iinc") { old ->
            // Change increment value to 2
            val oldInc = getInc(old)
            val oldIncValue = oldInc.increment
            oldInc.increment = 2
            // Reload and check that it changed
            invokeMethod(reloadTempClass(old.owner), "iinc") { new ->
                val newIncValue = getInc(new).increment
                assertEquals(2, newIncValue, "IncrementInsn#increment did not change values")
                println("IncrementInsn#increment changed from $oldIncValue -> $newIncValue")
            }
        }
    }

    @Test
    fun fieldNameChange() {
        val getField: (CtMethod) -> FieldInsn = { ctm ->
            val insn = ctm.instructions.findLast { it is FieldInsn && it.parent == "samples.FieldKt" }
            assertNotNull("FieldInsn not found", insn)
            insn as FieldInsn
        }

        invokeSampleMethod("samples.FieldKt", "printField") { old ->
            // change field name to field2
            val oldField = getField(old)
            val oldFieldName = oldField.name
            oldField.name = "field2"
            invokeMethod(reloadTempClass(old.owner), "printField") { new ->
                val newFieldName = getField(new).name
                assertEquals("field2", newFieldName, "FieldInsn#name did not change")
                println("FieldInsn#name changed from $oldFieldName -> $newFieldName")
            }
        }
    }

    @Test
    fun methodNameChange() {
        val getInvoke: (CtMethod) -> MethodInsn = { ctm ->
            val insn = ctm.instructions.findLast { it is MethodInsn && it.parent == "samples.MethodKt" }
            assertNotNull("MethodInsn not found", insn)
            insn as MethodInsn
        }

        invokeSampleMethod("samples.MethodKt", "invokeMethod") { old ->
            // change method name to b
            val oldMethod = getInvoke(old)
            val oldMethodName = oldMethod.name
            oldMethod.name = "b"
            invokeMethod(reloadTempClass(old.owner), "invokeMethod") { new ->
                val newMethodName = getInvoke(new).name
                assertEquals("b", newMethodName, "MethodInsn#name did not change")
                println("MethodInsn#name changed from $oldMethodName -> $newMethodName")
            }
        }
    }

    @Test
    fun ldcValueChange() {
        val getLdc: (CtMethod) -> LdcInsn = { ctm ->
            val insn = ctm.instructions.findLast { it is LdcInsn }
            assertNotNull("LdcInsn not found", insn)
            insn as LdcInsn
        }

        invokeSampleMethod("samples.LdcKt", "printString") { old ->
            // change ldc value to Nevermind!
            val oldLdc = getLdc(old)
            val oldValue = oldLdc.cst
            oldLdc.setString("Nevermind!")
            invokeMethod(reloadTempClass(old.owner), "printString") { new ->
                val newValue = getLdc(new).cst
                assertEquals("Nevermind!", newValue, "LdcInsn#cst did not change")
                println("LdcInsn#cst changed from $oldValue -> $newValue")
            }
        }
    }

    @Test
    fun varValueChange() {
        val getStore: (CtMethod) -> VarInsn = { ctm ->
            val insn = ctm.instructions.findLast { it is VarInsn && it.opname.startsWith("istore") }
            assertNotNull("VarInsn not found", insn)
            insn as VarInsn
        }

        invokeSampleMethod("samples.VarKt", "swap") { old ->
            // change istore value to 1
            val oldVar = getStore(old)
            val oldVarValue = oldVar.variable
            oldVar.variable = 1
            invokeMethod(reloadTempClass(old.owner), "swap") { new ->
                val newVarValue = getStore(new).variable
                assertEquals(1, newVarValue, "VarInsn#variable did not change")
                println("VarInsn#variable changed from $oldVarValue -> $newVarValue")
            }
        }
    }

    @Test
    fun typeDescChange() {
        val getType: (CtMethod) -> TypeInsn = { ctm ->
            val insn = ctm.instructions.findLast { it is TypeInsn }
            assertNotNull("TypeInsn not found", insn)
            insn as TypeInsn
        }

        invokeSampleMethod("samples.TypeKt", "makeBlankObject") { old ->
            // change type to java/lang/Object
            val oldType = getType(old)
            val oldTypeValue = oldType.type
            oldType.type = "java/lang/Object"
            invokeMethod(reloadTempClass(old.owner), "makeBlankObject") { new ->
                val newTypeValue = getType(new).type
                assertEquals("java.lang.Object", newTypeValue, "TypeInsn#type did not change")
                println("TypeInsn#type changed from $oldTypeValue -> $newTypeValue")
            }
        }
    }

    @Test
    fun intValueChange() {
        val getInt: (CtMethod) -> IntInsn = { ctm ->
            val insn = ctm.instructions.findLast { it is IntInsn }
            assertNotNull("IntInsn not found", insn)
            insn as IntInsn
        }

        invokeSampleMethod("samples.IntKt", "printBipush") { old ->
            // change value to byte max (255)
            val oldInt = getInt(old)
            val oldIntValue = oldInt.operand

            oldInt.operand = 255

            invokeMethod(reloadTempClass(old.owner), "printBipush") { new ->
                val newInt = getInt(new).operand
                assertEquals(255, newInt, "IntInsn#operand did not change (bipush->bipush)")
                println("IntInsn#operand changed from $oldIntValue -> $newInt (bipush->bipush)")
            }
        }

        invokeSampleMethod("samples.IntKt", "printBipush") { old ->
            // change value to byte max (255)
            val oldInt = getInt(old)
            val oldIntValue = oldInt.operand

            oldInt.operand = 256

            invokeMethod(reloadTempClass(old.owner), "printBipush") { new ->
                val newInt = getInt(new).operand
                assertEquals(256, newInt, "IntInsn#operand did not change (bipush->sipush)")
                println("IntInsn#operand changed from $oldIntValue -> $newInt (bipush->sipush)")
            }
        }

        invokeSampleMethod("samples.IntKt", "printSipush") { old ->
            // change value to byte max (255)
            val oldInt = getInt(old)
            val oldIntValue = oldInt.operand

            oldInt.operand = 40

            invokeMethod(reloadTempClass(old.owner), "printSipush") { new ->
                val newInt = getInt(new).operand
                assertEquals(40, newInt, "IntInsn#operand did not change (sipush->bipush)")
                println("IntInsn#operand changed from $oldIntValue -> $newInt (sipush->bipush)")
            }
        }
    }
}

val SAMPLES = CtClassScanner.scanDirectory(File("target/test-classes/samples/"))
const val TEMP_PATH = "./temp/samples/"

fun invokeMethod(ctc: CtClass, methodName: String, action: (CtMethod) -> Unit) {
    val ctMethod = ctc.allMethods.find { it.name == methodName }
    assertNotNull("Sample method <$methodName> was not found", ctMethod)
    action(ctMethod!!)
}

fun invokeSampleMethod(className: String, methodName: String, action: (CtMethod) -> Unit) {
    val ctClass = SAMPLES[className]
    assertNotNull("Sample class <$className> was not found", ctClass)
    val ctMethod = ctClass!!.allMethods.find { it.name == methodName }
    assertNotNull("Sample method <$methodName> was not found", ctMethod)
    action(ctMethod!!)
    ctClass.defrost()
}

fun reloadTempClass(ctc: CtClass): CtClass {
    // dump the class data to a new file
    val tempFile = File("$TEMP_PATH/${ctc.simpleName}.class")
    tempFile.parentFile.mkdirs()
    Files.write(tempFile.toPath(), ctc.toBytecode())
    // load the new file into memory
    val reloadedClass = CtClassScanner.scanClassFile(tempFile.absolutePath)
    assertNotNull("Failed to reload class <${ctc.simpleName}>", reloadedClass)

    reloadedClass?.stopPruning(true)
    reloadedClass?.defrost()

    // remove files after reloading to memory
    tempFile.delete()
    val dir = File(TEMP_PATH)
    if ((dir.listFiles() ?: emptyArray()).isEmpty()) {
        if (dir.parentFile.endsWith("temp")) { // safety check
            dir.delete()
            dir.parentFile.delete()
        }
    }
    return reloadedClass!!
}