package h04;

import fopbot.KeyPressListener;
import h04.util.H04TestBase;
import h04.util.reflect.StudentLinks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.objectweb.asm.*;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.sourcegrade.jagr.api.testing.ClassTransformer;
import org.sourcegrade.jagr.api.testing.TestCycle;
import org.sourcegrade.jagr.api.testing.extension.JagrExecutionCondition;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.Modifier;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.emptyContext;

@TestForSubmission
public class H3_3_Test extends H04TestBase {

    public static Object KEY_PRESS_LISTENER = null;
    public static boolean CALLED_ADD_KEY_PRESS_LISTENER = false;

    @Test
    public void testDeclaredCorrectly() {
        var link = StudentLinks.KEYBOARD_FIELD_SELECTOR_LINK.get();
        Assertions.assertEquals(link.reflection().getName(), "h04.selection.KeyboardFieldSelector", "Wrong identifier.");
        Assertions3.assertCorrectInterfaces(link, StudentLinks.FIELD_SELECTOR_LINK.get().reflection(), KeyPressListener.class);
    }

    @Test
    public void testFieldSelectionListener() throws Throwable {
        var link = StudentLinks.KEYBOARD_FIELD_SELECTOR_LINK.get();
        var fieldSelectionListenerField = link.getField(Matcher.of(fieldLink -> fieldLink.type().equals(StudentLinks.FIELD_SELECTOR_LISTENER_LINK.get())));
        var mockInstance = Mockito.mock(link.reflection(), Mockito.CALLS_REAL_METHODS);
        var fieldSelectionListenerInstance = Mockito.mock(StudentLinks.FIELD_SELECTOR_LISTENER_LINK.get().reflection());

        StudentLinks.FIELD_SELECTOR_SET_FIELD_SELECTION_LISTENER_LINK.get().invoke(mockInstance, fieldSelectionListenerInstance);
        assertSame(
            fieldSelectionListenerInstance,
            fieldSelectionListenerField.get(mockInstance),
            contextBuilder().add("fieldSelectionListener", fieldSelectionListenerInstance).build(),
            result -> "setFieldSelectionListener(FieldSelectionListener) did not set field %s to the expected value".formatted(fieldSelectionListenerField.name())
        );
    }

    @Test
    public void testOnKeyPress() throws Throwable {
        // TODO: implement test
    }

    @Test
    @ExtendWith(JagrExecutionCondition.class)
    @ExtendWith(TestCycleResolver.class)
    public void testConstructor(TestCycle testCycle) throws Throwable {
        var link = StudentLinks.KEYBOARD_FIELD_SELECTOR_LINK.get();
        Class<?> clazz = testCycle.getClassLoader().loadClass(link.reflection().getName(), new ClassTransformer() {
            @Override
            public String getName() {
                return "KeyboardFieldSelectorTransformer";
            }

            @Override
            public void transform(ClassReader reader, ClassWriter writer) {
                reader.accept(new ClassVisitor(Opcodes.ASM9, writer) {
                    @Override
                    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                        if (name.equals("<init>") &&
                            descriptor.equals("()V")) {
                            return new MethodVisitor(Opcodes.ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                                @Override
                                public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                                    if (opcode == Opcodes.INVOKESTATIC &&
                                        owner.equals("fopbot/World") &&
                                        name.equals("addKeyPressListener") &&
                                        descriptor.equals("(Lfopbot/KeyPressListener;)V")) {
                                        super.visitFieldInsn(Opcodes.PUTSTATIC,
                                            "h04/H3_3_Test",
                                            "KEY_PRESS_LISTENER",
                                            "Ljava/lang/Object;");
                                        super.visitInsn(Opcodes.ICONST_1);
                                        super.visitFieldInsn(Opcodes.PUTSTATIC,
                                            "h04/H3_3_Test",
                                            "CALLED_ADD_KEY_PRESS_LISTENER",
                                            "Z");
                                    } else {
                                        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                                    }
                                }
                            };
                        } else {
                            return super.visitMethod(access, name, descriptor, signature, exceptions);
                        }
                    }
                }, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            }
        });
        var constructor = link.getConstructor(Matcher.of(constructorLink -> constructorLink.typeList().isEmpty()));
        Assertions3.assertCorrectModifiers(constructor, Modifier.PUBLIC);

        var instance = clazz.getDeclaredConstructor().newInstance();
        assertTrue(CALLED_ADD_KEY_PRESS_LISTENER, emptyContext(), result ->
            "World.addKeyPressListener(...) was not called in the constructor");
        assertSame(instance, KEY_PRESS_LISTENER, emptyContext(), result ->
            "World.addKeyPressListener(...) was not called with the correct value (this)");
    }
}
