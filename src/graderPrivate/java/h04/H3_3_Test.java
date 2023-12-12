package h04;

import fopbot.*;
import h04.util.H04TestBase;
import h04.util.reflect.StudentLinks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.objectweb.asm.*;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.sourcegrade.jagr.api.testing.ClassTransformer;
import org.sourcegrade.jagr.api.testing.TestCycle;
import org.sourcegrade.jagr.api.testing.extension.JagrExecutionCondition;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.Modifier;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

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

    @ParameterizedTest
    @ExtendWith(JagrExecutionCondition.class)
    @JsonParameterSetTest(value = "H3_3.json", customConverters = "customConverters")
    public void testOnKeyPress(JsonParameterSet params) throws Throwable {
        setupWorld(params.getInt("worldWidth"), params.getInt("worldHeight"));
        Key[] keys = Key.values();
        Map<Key, KeyPressEvent> keyPressEvents = Map.of(
            Key.SPACE, new KeyPressEvent(World.getGlobalWorld(), Key.SPACE),
            Key.LEFT, new KeyPressEvent(World.getGlobalWorld(), Key.LEFT),
            Key.RIGHT, new KeyPressEvent(World.getGlobalWorld(), Key.RIGHT),
            Key.UP, new KeyPressEvent(World.getGlobalWorld(), Key.UP),
            Key.DOWN, new KeyPressEvent(World.getGlobalWorld(), Key.DOWN)
        );

        var link = StudentLinks.KEYBOARD_FIELD_SELECTOR_LINK.get();

        var keyboardFieldSelectorInstance = Mockito.mock(link.reflection(), Mockito.CALLS_REAL_METHODS);
        var onKeyPressMethod = BasicMethodLink.of(KeyPressListener.class.getMethod("onKeyPress", KeyPressEvent.class));

        var actualX = new AtomicInteger();
        var actualY = new AtomicInteger();
        var fieldSelectionListener = Mockito.mock(StudentLinks.FIELD_SELECTOR_LISTENER_LINK.get().reflection(), (Answer<Object>) invocationOnMock -> {
            if (invocationOnMock.getMethod().equals(StudentLinks.FIELD_SELECTOR_LISTENER_ON_FIELD_SELECTION_LINK.get().reflection())) {
                Field field = invocationOnMock.getArgument(0);
                actualX.set(field.getX());
                actualY.set(field.getY());
                return null;
            } else {
                return invocationOnMock.callRealMethod();
            }
        });

        // set FieldSelectionListener in mocked class
        var listenerField = link.getField(Matcher.of(fieldLink -> fieldLink.type().equals(StudentLinks.FIELD_SELECTOR_LISTENER_LINK.get())));
        listenerField.set(keyboardFieldSelectorInstance, fieldSelectionListener);

        onKeyPressMethod.invoke(keyboardFieldSelectorInstance, keyPressEvents.get(Key.SPACE));
        for (Integer keyCode : params.get("movements", Integer[].class)) {
            Key key = keys[keyCode];

            onKeyPressMethod.invoke(keyboardFieldSelectorInstance, keyPressEvents.get(key));
        }
        onKeyPressMethod.invoke(keyboardFieldSelectorInstance, keyPressEvents.get(Key.SPACE));

        Context context = params.toContext();
        assertEquals(
            params.getInt("expectedX"),
            actualX.get(),
            context,
            result -> "X coordinate of selected field does not equal x coordinate of expected field"
        );
        assertEquals(
            params.getInt("expectedY"),
            actualY.get(),
            context,
            result -> "Y coordinate of selected field does not equal y coordinate of expected field"
        );
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
