package h04;

import fopbot.FieldClickEvent;
import fopbot.FieldClickListener;
import fopbot.World;
import h04.util.H04TestBase;
import h04.util.reflect.StudentLinks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.objectweb.asm.*;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.sourcegrade.jagr.api.testing.ClassTransformer;
import org.sourcegrade.jagr.api.testing.TestCycle;
import org.sourcegrade.jagr.api.testing.extension.JagrExecutionCondition;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.Modifier;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class H3_2_Test extends H04TestBase {

    public static Object FIELD_CLICK_LISTENER = null;
    public static boolean CALLED_ADD_FIELD_CLICK_LISTENER = false;

    @Test
    public void testDeclaredCorrectly() {
        var link = StudentLinks.MOUSE_FIELD_SELECTOR_LINK.get();
        Assertions.assertEquals(link.reflection().getName(), "h04.selection.MouseFieldSelector", "Wrong identifier.");
        Assertions3.assertCorrectInterfaces(link, StudentLinks.FIELD_SELECTOR_LINK.get().reflection(), FieldClickListener.class);
    }

    @Test
    public void testFieldSelectionListener() throws Throwable {
        var link = StudentLinks.MOUSE_FIELD_SELECTOR_LINK.get();
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
    public void testOnFieldClick() throws Throwable {
        setupWorld(5, 5);
        var link = StudentLinks.MOUSE_FIELD_SELECTOR_LINK.get();
        var mouseFieldSelectorInstance = Mockito.mock(link.reflection(), Mockito.CALLS_REAL_METHODS);
        var onFieldClickMethod = BasicMethodLink.of(FieldClickListener.class.getMethod("onFieldClick", FieldClickEvent.class));
        var field = World.getGlobalWorld().getField(4, 2);
        var fieldClickEvent = new FieldClickEvent(field);
        var fieldSelectionListenerCalled = new AtomicBoolean(false);
        var fieldSelectionListener = Mockito.mock(StudentLinks.FIELD_SELECTOR_LISTENER_LINK.get().reflection(), (Answer<Object>) invocationOnMock -> {
            if (invocationOnMock.getMethod().equals(StudentLinks.FIELD_SELECTOR_LISTENER_ON_FIELD_SELECTION_LINK.get().reflection())) {
                fieldSelectionListenerCalled.set(true);
                return null;
            } else {
                return invocationOnMock.callRealMethod();
            }
        });

        // set FieldSelectionListener in mocked class
        var listenerField = link.getField(Matcher.of(fieldLink -> fieldLink.type().equals(StudentLinks.FIELD_SELECTOR_LISTENER_LINK.get())));
        listenerField.set(mouseFieldSelectorInstance, fieldSelectionListener);

        var context = contextBuilder()
            .add("fieldClickEvent", fieldClickEvent)
            .build();

        // TODO: test different fields

        onFieldClickMethod.invoke(mouseFieldSelectorInstance, fieldClickEvent);
        assertFalse(fieldSelectionListenerCalled.get(), context, result ->
            "onFieldClick(FieldClickEvent) called the registered FieldSelectionListener but it wasn't supposed to (first click)");
        onFieldClickMethod.invoke(mouseFieldSelectorInstance, fieldClickEvent);
        assertTrue(fieldSelectionListenerCalled.get(), context, result ->
            "onFieldClick(FieldClickEvent) did not call the registered FieldSelectionListener but it was supposed to (second click)");
    }

    @Test
    @ExtendWith(JagrExecutionCondition.class)
    @ExtendWith(TestCycleResolver.class)
    public void testConstructor(TestCycle testCycle) throws Throwable {
        var link = StudentLinks.MOUSE_FIELD_SELECTOR_LINK.get();
        Class<?> clazz = testCycle.getClassLoader().loadClass(link.reflection().getName(), new ClassTransformer() {
            @Override
            public String getName() {
                return "MouseFieldSelectorTransformer";
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
                                        name.equals("addFieldClickListener") &&
                                        descriptor.equals("(Lfopbot/FieldClickListener;)V")) {
                                        super.visitFieldInsn(Opcodes.PUTSTATIC,
                                            "h04/H3_2_Test",
                                            "FIELD_CLICK_LISTENER",
                                            "Ljava/lang/Object;");
                                        super.visitInsn(Opcodes.ICONST_1);
                                        super.visitFieldInsn(Opcodes.PUTSTATIC,
                                            "h04/H3_2_Test",
                                            "CALLED_ADD_FIELD_CLICK_LISTENER",
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
        assertTrue(CALLED_ADD_FIELD_CLICK_LISTENER, emptyContext(), result ->
            "World.addFieldClickListener(...) was not called in the constructor");
        assertSame(instance, FIELD_CLICK_LISTENER, emptyContext(), result ->
            "World.addFieldClickListener(...) was not called with the correct value (this)");
    }
}
