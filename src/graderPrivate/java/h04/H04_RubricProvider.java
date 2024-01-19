package h04;

import h04.util.reflect.Global;
import org.objectweb.asm.*;
import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;
import org.sourcegrade.jagr.api.testing.ClassTransformer;
import org.sourcegrade.jagr.api.testing.RubricConfiguration;
import org.sourcegrade.jagr.api.testing.TestCycle;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;

import static org.tudalgo.algoutils.tutor.general.jagr.RubricUtils.criterion;

public class H04_RubricProvider implements RubricProvider {

    public static final Rubric RUBRIC = Rubric.builder()
        .title("H04 | Die Übung mit der Maus")
        .addChildCriteria(
            Criterion.builder()
                .shortDescription("H1 | Move Strategies")
                .addChildCriteria(
                    Criterion.builder()
                        .shortDescription("H1.1 | Interface für Move Strategies")
                        .addChildCriteria(
                            criterion(
                                "Das Interface MoveStrategy wurde korrekt deklariert.",
                                JUnitTestRef.ofMethod(() -> H1_1_Test.class.getMethod("testDeclaredCorrectly"))
                            ),
                            criterion(
                                "Die Methode start() wurde korrekt deklariert.",
                                JUnitTestRef.ofMethod(() -> H1_1_Test.class.getMethod("testStartMethodDeclaredCorrectly"))
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H1.2 | Interface für Move MoveStrategyWithCounter mit Counters")
                        .addChildCriteria(
                            criterion(
                                "Das Interface MoveStrategyWithCounter wurde korrekt deklariert.",
                                JUnitTestRef.ofMethod(() -> H1_2_Test.class.getMethod("testDeclaredCorrectly"))
                            ),
                            criterion(
                                "Die Methode getMoveCount() wurde korrekt deklariert.",
                                JUnitTestRef.ofMethod(() -> H1_2_Test.class.getMethod("testGetMoveCountMethodDeclaredCorrectly"))
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H1.3 | Move me by a Teleport!")
                        .addChildCriteria(
                            criterion(
                                "Die Klasse MoveByTeleport wurde korrekt deklariert.",
                                JUnitTestRef.ofMethod(() -> H1_3_Test.class.getMethod("testDeclaredCorrectly"))
                            ),
                            criterion(
                                "Nach dem Aufruf der Methode start() befindet sich der Roboter auf dem gegebenen Feld.",
                                JUnitTestRef.ofMethod(() -> H1_3_Test.class.getMethod("testStartMethodNonStrict", JsonParameterSet.class))
                            ),
                            criterion(
                                "Die Methode start() ist vollständig korrekt implementiert.",
                                JUnitTestRef.ofMethod(() -> H1_3_Test.class.getMethod("testStartMethodStrict", JsonParameterSet.class))
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H1.4 | Move me by a Walk!")
                        .addChildCriteria(
                            criterion(
                                "Die Klasse MoveByWalk wurde korrekt deklariert.",
                                JUnitTestRef.ofMethod(() -> H1_4_Test.class.getMethod("testDeclaredCorrectly"))
                            ),
                            criterion(
                                "Nach dem Aufruf der Methode start() befindet sich der Roboter auf dem gegebenen Feld.",
                                JUnitTestRef.ofMethod(() -> H1_4_Test.class.getMethod("testStartMethodNonStrict", JsonParameterSet.class))
                            ),
                            criterion(
                                "Die Methode start() ist vollständig korrekt implementiert.",
                                JUnitTestRef.ofMethod(() -> H1_4_Test.class.getMethod("testStartMethodStrict", JsonParameterSet.class))
                            ),
                            criterion(
                                "Die Methode getMoveCount() ist vollständig korrekt implementiert.",
                                JUnitTestRef.ofMethod(() -> H1_4_Test.class.getMethod("testGetMoveCount", JsonParameterSet.class))
                            )
                        )
                        .build()
                )
                .build(),
            Criterion.builder()
                .shortDescription("H2 | Field Selection Listeners")
                .addChildCriteria(
                    Criterion.builder()
                        .shortDescription("H2.1 | Interface für Field Selection Listeners")
                        .addChildCriteria(
                            criterion(
                                "Das Interface MoveStrategy wurde korrekt deklariert.",
                                JUnitTestRef.ofMethod(() -> H2_1_Test.class.getDeclaredMethod("testDeclaredCorrectly"))
                            ),
                            criterion(
                                "Die Methode onFieldSelection() wurde korrekt deklariert.",
                                JUnitTestRef.ofMethod(() -> H2_1_Test.class.getDeclaredMethod("testOnFieldSelectionDeclaredCorrectly"))
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H2.2 | Robot Mover")
                        .addChildCriteria(
                            criterion(
                                "Die Klasse RobotMover wurde korrekt deklariert.",
                                JUnitTestRef.ofMethod(() -> H2_2_Test.class.getDeclaredMethod("testDeclaredCorrectly")),
                                JUnitTestRef.ofMethod(() -> H2_2_Test.class.getDeclaredMethod("testAddRobotDeclaration"))
                            ),
                            criterion(
                                "Der Konstruktor ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> H2_2_Test.class.getDeclaredMethod("testConstructor"))
                            ),
                            criterion(
                                "Die Methode addRobot() ist vollständig korrekt.",
                                JUnitTestRef.or(
                                    JUnitTestRef.ofMethod(() -> H2_2_Test.class.getDeclaredMethod("testAddRobotFunctionality1")),
                                    JUnitTestRef.ofMethod(() -> H2_2_Test.class.getDeclaredMethod("testAddRobotFunctionality2"))
                                )
                            ),
                            criterion(
                                "Die Methode onFieldSelection() ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> H2_2_Test.class.getDeclaredMethod("testOnFieldSelection"))
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H2.3 | Moveable Robot")
                        .addChildCriteria(
                            criterion(
                                "Die Klasse MoveableRobot wurde korrekt deklariert.",
                                JUnitTestRef.ofMethod(() -> H2_3_Test.class.getDeclaredMethod("testDeclaredCorrectly"))
                            ),
                            criterion(
                                "Der Konstruktor ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> H2_3_Test.class.getDeclaredMethod("testConstructor"))
                            ),
                            criterion(
                                "Die Methode onFieldSelection() funktioniert korrekt, wenn die im Konstruktor übergebene MovementStrategy eine MoveStrategyWithCounter ist.",
                                JUnitTestRef.ofMethod(() -> H2_3_Test.class.getDeclaredMethod("testOnFieldSelectionAlt"))
                            ),
                            criterion(
                                "Die Methode onFieldSelection() funktioniert korrekt, wenn die im Konstruktor übergebene MovementStrategy keine MoveStrategyWithCounter ist.",
                                JUnitTestRef.ofMethod(() -> H2_3_Test.class.getDeclaredMethod("testOnFieldSelectionNormal"))
                            )
                        )
                        .build()
                )
                .build(),
            Criterion.builder()
                .shortDescription("H3 | Field Selectors")
                .addChildCriteria(
                    Criterion.builder()
                        .shortDescription("H3.1 | Interface für Field Selectors ")
                        .addChildCriteria(
                            criterion(
                                "Das Interface FieldSelector wurde korrekt deklariert.",
                                JUnitTestRef.ofMethod(() -> H3_1_Test.class.getDeclaredMethod("testDeclaredCorrectly"))
                            ),
                            criterion(
                                "Die Methode setFieldSelectionListener() wurde korrekt deklariert.",
                                JUnitTestRef.ofMethod(() -> H3_1_Test.class.getDeclaredMethod("testSetFieldSelectionListenerDeclaredCorrectly"))
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H3.2 | Die Eingabe mit der Maus ... ")
                        .addChildCriteria(
                            criterion(
                                "Die Klasse MouseFieldSelector wurde korrekt deklariert.",
                                JUnitTestRef.ofMethod(() -> H3_2_Test.class.getDeclaredMethod("testDeclaredCorrectly"))
                            ),
                            criterion(
                                "Der Konstruktor ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> H3_2_Test.class.getDeclaredMethod("testConstructor", TestCycle.class))
                            ),
                            criterion(
                                "Die Methode setFieldSelectionListener() ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> H3_2_Test.class.getDeclaredMethod("testFieldSelectionListener"))
                            ),
                            criterion(
                                "Die Methode onFieldClick() ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> H3_2_Test.class.getDeclaredMethod("testOnFieldClick", JsonParameterSet.class))
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H3.3 | ... und der Tastatur")
                        .addChildCriteria(
                            criterion(
                                "Die Klasse KeyboardFieldSelector wurde korrekt deklariert.",
                                JUnitTestRef.ofMethod(() -> H3_3_Test.class.getDeclaredMethod("testDeclaredCorrectly"))
                            ),
                            criterion(
                                "Der Konstruktor ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> H3_3_Test.class.getDeclaredMethod("testConstructor", TestCycle.class))
                            ),
                            criterion(
                                "Die Methode setFieldSelectionListener() ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> H3_3_Test.class.getDeclaredMethod("testFieldSelectionListener"))
                            ),
                            criterion(
                                "Die Methode onKeyPress() ist vollständig korrekt.",
                                JUnitTestRef.ofMethod(() -> H3_3_Test.class.getDeclaredMethod("testOnKeyPress", JsonParameterSet.class))
                            )
                        )
                        .build()
                )
                .build(),
            Criterion.builder()
                .shortDescription("H4 | It’s better together!")
                .addChildCriteria(
                    criterion(
                        "Die Klasse Main führt die geforderten Aktionen korrekt aus.",
                        JUnitTestRef.ofMethod(() -> H4_Test.class.getDeclaredMethod("testMain01")),
                        JUnitTestRef.ofMethod(() -> H4_Test.class.getDeclaredMethod("testMain02"))
                    )
                )
                .build()
        )
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }

    @Override
    public void configure(RubricConfiguration configuration) {
        configuration.addTransformer(new ClassTransformer() {
            @Override
            public String getName() {
                return "KeyboardFieldSelectorTransformer";
            }

            @Override
            public int getWriterFlags() {
                return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
            }

            @Override
            public void transform(ClassReader reader, ClassWriter writer) {
                if (Global.similarityMatcher("h04/selection/KeyboardFieldSelector").match(reader::getClassName).matched()) {
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
                } else {
                    reader.accept(writer, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                }
            }
        });
        configuration.addTransformer(new ClassTransformer() {
            @Override
            public String getName() {
                return "MouseFieldSelectorTransformer";
            }

            @Override
            public int getWriterFlags() {
                return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
            }

            @Override
            public void transform(ClassReader reader, ClassWriter writer) {
                if (!Global.similarityMatcher("h04/selection/MouseFieldSelector").match(reader::getClassName).matched()) {
                    reader.accept(writer, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                    return;
                }

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
        configuration.addTransformer(new ClassTransformer() {
            @Override
            public String getName() {
                return "FieldColorTransformer";
            }

            @Override
            public int getWriterFlags() {
                return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
            }

            @Override
            public void transform(ClassReader reader, ClassWriter writer) {
                if (reader.getClassName().startsWith("h04")) {
                    reader.accept(new ClassVisitor(Opcodes.ASM9, writer) {
                        @Override
                        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                            return new MethodVisitor(Opcodes.ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                                @Override
                                public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                                    if (opcode == Opcodes.INVOKEVIRTUAL &&
                                        owner.equals("fopbot/Field") &&
                                        name.equals("setFieldColor") &&
                                        descriptor.equals("(Ljava/awt/Color;)V")) {
                                        super.visitInsn(Opcodes.POP);
                                        super.visitInsn(Opcodes.POP);
                                    } else if (opcode == Opcodes.INVOKEVIRTUAL &&
                                        owner.equals("fopbot/KarelWorld") &&
                                        name.equals("setFieldColor") &&
                                        descriptor.equals("(IILjava/awt/Color;)V")) {
                                        super.visitInsn(Opcodes.POP);
                                        super.visitInsn(Opcodes.POP);
                                        super.visitInsn(Opcodes.POP);
                                        super.visitInsn(Opcodes.POP);
                                    } else {
                                        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                                    }
                                }
                            };
                        }
                    }, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                } else {
                    reader.accept(writer, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                }
            }
        });
        RubricProvider.super.configure(configuration);
    }
}
