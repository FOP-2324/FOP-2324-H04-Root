package h04;

import org.sourcegrade.jagr.api.rubric.*;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;

import static org.tudalgo.algoutils.tutor.general.jagr.RubricUtils.criterion;
import static org.tudalgo.algoutils.tutor.general.jagr.RubricUtils.defaultCriterionBuilder;

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
                                "Die Klasse MoveByWalk wurde korrekt deklariert."
                            ),
                            criterion(
                                "Beide interfaces wurden implementiert. (nicht zwingend korrekt)"
                            ),
                            criterion(
                                "Die Methode start() ist vollständig korrekt implementiert."
                            ),
                            criterion(
                                "Die Methode getMoveCount() ist vollständig korrekt implementiert."
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
                                "Das Interface MoveStrategy wurde korrekt deklariert."
                            ),
                            criterion(
                                "Die Methode onFieldSelection() wurde korrekt deklariert."
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H2.2 | Robot Mover")
                        .addChildCriteria(
                            criterion(
                                "Die Klasse RobotMover wurde korrekt deklariert."
                            ),
                            criterion(
                                "Der Konstruktor ist vollständig korrekt."
                            ),
                            criterion(
                                "Die Methode addRobot() ist vollständig korrekt."
                            ),
                            criterion(
                                "Die Methode onFieldSelection() ist vollständig korrekt."
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H2.3 | Moveable Robot")
                        .addChildCriteria(
                            criterion(
                                "Die Klasse MoveableRobot wurde korrekt deklariert."
                            ),
                            criterion(
                                "Der Konstruktor ist vollständig korrekt."
                            ),
                            criterion(
                                "Die Methode onFieldSelection() fuktioniert korrekt, wenn die im Konstruktor übergebene MovementStrategy eine MoveStrategyWithCounter ist."
                            ),
                            criterion(
                                "Die Methode onFieldSelection() fuktioniert korrekt, wenn die im Konstruktor übergebene MovementStrategy keine MoveStrategyWithCounter ist."
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
                                "Das Interface FieldSelector wurde korrekt deklariert."
                            ),
                            criterion(
                                "Die Methode setFieldSelectionListener() wurde korrekt deklariert."
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H3.2 | Die Eingabe mit der Maus ... ")
                        .addChildCriteria(
                            criterion(
                                "Die Klasse MouseFieldSelector wurde korrekt deklariert."
                            ),
                            criterion(
                                "Der Konstruktor ist vollständig korrekt."
                            ),
                            criterion(
                                "Die Methode setFieldSelectionListener() ist vollständig korrekt."
                            ),
                            criterion(
                                "Die Methode onFieldClick() ist vollständig korrekt."
                            )
                        )
                        .build(),
                    Criterion.builder()
                        .shortDescription("H3.3 | ... und der Tastatur")
                        .addChildCriteria(
                            criterion(
                                "Die Klasse KeyboardFieldSelector wurde korrekt deklariert."
                            ),
                            criterion(
                                "Der Konstruktor ist vollständig korrekt."
                            ),
                            criterion(
                                "Die Methode setFieldSelectionListener() ist vollständig korrekt."
                            ),
                            criterion(
                                "Die Methode onKeyPressed() ist vollständig korrekt."
                            )
                        )
                        .build()
                )
                .build(),
            Criterion.builder()
                .shortDescription("H4 | It’s better together!")
                .addChildCriteria(
                    criterion(
                        "Die Klasse Main führt die geforderten Aktionen korrekt aus."
                    )
                )
                .build(),
            Criterion.builder()
                .shortDescription("Global")
                .addChildCriteria(
                    defaultCriterionBuilder("Alle Elemente sind korrekt dokumentiert.")
                        .minPoints(-3)
                        .maxPoints(0)
                        .grader((dc, c) -> GradeResult.of(-3, 0, "This criterion will be graded manually."))
                        .build(),
                    defaultCriterionBuilder("Alle Identifier sind korrekt.")
                        .minPoints(-3)
                        .maxPoints(0)
                        .grader((dc, c) -> GradeResult.of(-3, 0, "This criterion will be graded manually."))
                        .build()
                )
                .build()
        )
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }
}
