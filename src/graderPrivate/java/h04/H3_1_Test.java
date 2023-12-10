package h04;

import fopbot.Field;
import fopbot.Robot;
import h04.selection.FieldSelectionListener;
import h04.util.TestConstants;
import h04.util.reflect.StudentLinks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.match.BasicReflectionMatchers;
import org.tudalgo.algoutils.tutor.general.reflections.Modifier;

import java.util.concurrent.TimeUnit;

@TestForSubmission
@Timeout(
    value = TestConstants.TEST_TIMEOUT_IN_SECONDS,
    unit = TimeUnit.SECONDS,
    threadMode = Timeout.ThreadMode.SEPARATE_THREAD
)
public class H3_1_Test {

    @Test
    public void testDeclaredCorrectly() {
        var link = StudentLinks.FIELD_SELECTOR_LINK.get();
        Assertions.assertEquals(link.reflection().getName(), "h04.selection.FieldSelector", "Wrong identifier.");
        Assertions3.assertCorrectModifiers(link, Modifier.PUBLIC, Modifier.INTERFACE);
    }

    @Test
    public void testSetFieldSelectionListenerDeclaredCorrectly() {
        final var link = StudentLinks.FIELD_SELECTOR_SET_FIELD_SELECTION_LISTENER_LINK.get();
        Assertions.assertEquals(link.reflection().getName(), "setFieldSelectionListener", "Wrong identifier.");
        Assertions3.assertCorrectModifiers(link, Modifier.PUBLIC);
        Assertions3.assertCorrectReturnType(link, void.class);
        Assertions3.assertCorrectParameters(link, BasicReflectionMatchers.sameType(StudentLinks.FIELD_SELECTOR_LISTENER_LINK.get().reflection()));
    }
}
