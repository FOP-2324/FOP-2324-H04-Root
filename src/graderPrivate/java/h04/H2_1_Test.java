package h04;

import fopbot.Field;
import h04.util.H04TestBase;
import h04.util.reflect.StudentLinks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.match.BasicReflectionMatchers;
import org.tudalgo.algoutils.tutor.general.reflections.Modifier;

@TestForSubmission
public class H2_1_Test extends H04TestBase {

    @Test
    public void testDeclaredCorrectly() {
        var link = StudentLinks.FIELD_SELECTOR_LISTENER_LINK.get();
        Assertions.assertEquals(
            link.reflection().getName(),
            "h04.selection.FieldSelectionListener",
            "Wrong identifier."
        );
        Assertions3.assertCorrectModifiers(link, Modifier.PUBLIC, Modifier.INTERFACE);
    }

    @Test
    public void testOnFieldSelectionDeclaredCorrectly() {
        final var link = StudentLinks.FIELD_SELECTOR_LISTENER_ON_FIELD_SELECTION_LINK.get();
        Assertions.assertEquals(link.reflection().getName(), "onFieldSelection", "Wrong identifier.");
        Assertions3.assertCorrectModifiers(link, Modifier.PUBLIC);
        Assertions3.assertCorrectReturnType(link, void.class);
        Assertions3.assertCorrectParameters(link, BasicReflectionMatchers.sameType(Field.class));
    }
}
