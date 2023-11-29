package h04;

import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import static org.tudalgo.algoutils.tutor.general.jagr.RubricUtils.criterion;

public class H04_RubricProvider implements RubricProvider {

    public static final Rubric RUBRIC = Rubric.builder()
        .title("H02 | Cleaning Convoy")
        .addChildCriteria(
            Criterion.builder()
                .shortDescription("H1 | Erstellen der Roboter-Arrays")
                .addChildCriteria(
                    Criterion.builder()
                        .shortDescription("H1.1 | ScanRobots")
                        .addChildCriteria(
                            criterion(
                                "Das Array hat die korrekte Länge der Breite der Welt."
                            ),
                            criterion(
                                "Es stehen auf keinem Feld zwei Roboter."
                            ),
                            criterion(
                                "Die Roboter sind alle richtig initialisiert (Koordinaten, Ausrichtung und"
                                    + " keine Münzen)."
                            )
                        )
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
