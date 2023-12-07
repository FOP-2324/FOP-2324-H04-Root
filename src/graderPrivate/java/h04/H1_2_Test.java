package h04;

import fopbot.Field;
import fopbot.Robot;
import h04.util.StudentLinks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.match.BasicReflectionMatchers;
import org.tudalgo.algoutils.tutor.general.reflections.Modifier;

public class H1_2_Test {

    @Test
    public void testDeclaredCorrectly() {
        var link = StudentLinks.MOVE_STRATEGY_WITH_COUNTER_LINK.get();
        Assertions.assertEquals(
            link.reflection().getName(),
            "h04.strategy.MoveStrategyWithCounter",
            "Wrong identifier."
        );
        Assertions3.assertCorrectModifiers(link, Modifier.PUBLIC, Modifier.INTERFACE);
        Assertions3.assertCorrectInterfaces(link, StudentLinks.MOVE_STRATEGY_LINK.get());
    }

    @Test
    public void testGetMoveCountMethodDeclaredCorrectly() {
        final var link = StudentLinks.MOVE_STRATEGY_WITH_COUNTER_GET_MOVE_COUNT_LINK.get();
        Assertions.assertEquals(link.reflection().getName(), "getMoveCount", "Wrong identifier.");
        Assertions3.assertCorrectModifiers(link, Modifier.PUBLIC);
        Assertions3.assertCorrectReturnType(link, int.class);
        Assertions3.assertCorrectParameters(link);
    }
}
