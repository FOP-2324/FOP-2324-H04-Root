package h04;

import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;

public class H04_RubricProvider_Javadoc implements RubricProvider {

    public static final Rubric RUBRIC = Rubric.builder()
        .title("H04 | Die Übung mit der Maus")
        .addChildCriteria(
            Criterion.builder()
                .shortDescription("Documentation")
//                .grader(
//                    Grader.testAwareBuilder()
//                        .requirePass(JUnitTestRef.ofMethod(() -> Collector.class.getMethod("collect")))
//                        .pointsFailedMin()
//                        .pointsFailedMax()
//                        .build()
//                )
                .build()
        )
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }
}
