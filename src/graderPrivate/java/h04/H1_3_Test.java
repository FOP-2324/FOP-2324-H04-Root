package h04;

import fopbot.Direction;
import fopbot.Robot;
import fopbot.World;
import h04.util.H04TestBase;
import h04.util.reflect.StudentLinks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.opentest4j.AssertionFailedError;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions4;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.reflections.Modifier;

import java.awt.*;

import static org.mockito.Mockito.spy;

@TestForSubmission
public class H1_3_Test extends H04TestBase {

    @Test
    public void testDeclaredCorrectly() {
        var link = StudentLinks.MOVE_BY_TELEPORT_LINK.get();
        Assertions.assertEquals(link.reflection().getName(), "h04.strategy.MoveByTeleport", "Wrong identifier.");
        Assertions3.assertCorrectModifiers(link, Modifier.PUBLIC, Modifier.CLASS, Modifier.NON_ABSTRACT);
        Assertions3.assertCorrectInterfaces(link, StudentLinks.MOVE_STRATEGY_LINK.get());
    }

    private void testStartMethod(JsonParameterSet params, boolean strict) {
        final var link = StudentLinks.MOVE_BY_TELEPORT_START_LINK.get();
        final var classLink = StudentLinks.MOVE_BY_TELEPORT_LINK.get();
        setupWorld(params.getInt("worldWidth"), params.getInt("worldHeight"));
        final var robot = params.get("robot", Robot.class);
        final var oldRobotFamily = robot.getFamily();
        final var oldRobotCoinAmount = robot.getNumberOfCoins();
        final var expectedEndPosition = params.get("expectedEndPosition", Point.class);
        final var expectedEndDirection = params.get("expectedEndDirection", Direction.class);
        final var field = World.getGlobalWorld().getField(expectedEndPosition.x, expectedEndPosition.y);

        var paramsContext = params.toContext("robot");
        var cb = Assertions2.contextBuilder()
            .add(paramsContext)
            .add("robot (before call)", robot.toString())
            .add("robot (after call)", robot)
            .add("field", field);

        var context = cb.build();
        try {
            Assertions3.assertImplementsInterfaceMethod(classLink, StudentLinks.MOVE_STRATEGY_START_LINK.get());
        } catch (AssertionFailedError e) {
            if (!e.getMessage().contains("@Override")) {
                throw e;
            }
        }
        Assertions4.assertElementsNotUsed(
            link.getCtElement(),
            context,
            Assertions4.buildCtElementBlacklist(
                () -> Robot.class.getMethod("move")
            )
        );
        final var instance = spy(classLink.reflection());
        Assertions2.call(
            link,
            instance,
            context,
            r -> "The method threw an exception during execution.",
            robot,
            field
        );

        Assertions2.assertEquals(
            expectedEndPosition.x,
            robot.getX(),
            context,
            r -> "The robot did not end up at the expected x position."
        );

        Assertions2.assertEquals(
            expectedEndPosition.y,
            robot.getY(),
            context,
            r -> "The robot did not end up at the expected y position."
        );

        if (strict) {
            // end direction
            Assertions2.assertEquals(
                expectedEndDirection,
                robot.getDirection(),
                context,
                r -> "The robot direction changed."
            );

            // coins
            Assertions2.assertEquals(
                oldRobotCoinAmount,
                robot.getNumberOfCoins(),
                context,
                r -> "The robot coin amount changed."
            );

            // family
            Assertions2.assertEquals(
                oldRobotFamily,
                robot.getRobotFamily(),
                context,
                r -> "The robot family changed."
            );
        }
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H1_3.json", customConverters = "customConverters")
    public void testStartMethodNonStrict(final JsonParameterSet params) {
        testStartMethod(params, false);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H1_3.json", customConverters = "customConverters")
    public void testStartMethodStrict(final JsonParameterSet params) {
        testStartMethod(params, true);
    }
}
