package h04;

import fopbot.Direction;
import fopbot.Robot;
import fopbot.RobotFamily;
import fopbot.World;
import h04.util.H04TestBase;
import h04.util.TestConstants;
import h04.util.reflect.StudentLinks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions4;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.reflections.Modifier;

import java.awt.Point;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.spy;

@TestForSubmission
@Timeout(
    value = TestConstants.TEST_TIMEOUT_IN_SECONDS,
    unit = TimeUnit.SECONDS,
    threadMode = Timeout.ThreadMode.SEPARATE_THREAD
)
public class H1_4_Test extends H04TestBase {

    @Test
    public void testDeclaredCorrectly() {
        var link = StudentLinks.MOVE_BY_WALK_LINK.get();
        Assertions.assertEquals(link.reflection().getName(), "h04.strategy.MoveByWalk", "Wrong identifier.");
        Assertions3.assertCorrectModifiers(link, Modifier.PUBLIC, Modifier.CLASS, Modifier.NON_ABSTRACT);
        Assertions3.assertCorrectInterfaces(
            link,
            StudentLinks.MOVE_STRATEGY_WITH_COUNTER_LINK.get()
        );
    }

    private final static class MovementCountingRobot extends Robot {
        private int moveCount = 0;

        public MovementCountingRobot(int x, int y, Direction direction, int numberOfCoins, RobotFamily robotFamily) {
            super(x, y, direction, numberOfCoins, robotFamily);
        }

        @Override
        public void move() {
            super.move();
            moveCount++;
        }

        public int getMoveCount() {
            return moveCount;
        }
    }

    private void testStartMethod(JsonParameterSet params, boolean testStart, boolean strict, boolean testGetMoveCount) {
        final var link = StudentLinks.MOVE_BY_WALK_START_LINK.get();
        final var classLink = StudentLinks.MOVE_BY_WALK_LINK.get();
        setupWorld(params.getInt("worldWidth"), params.getInt("worldHeight"));
        var robot = params.get("robot", Robot.class);
        robot = new MovementCountingRobot(
            robot.getX(),
            robot.getY(),
            robot.getDirection(),
            robot.getNumberOfCoins(),
            robot.getRobotFamily()
        );
        final var oldRobotFamily = robot.getRobotFamily();
        final var oldRobotCoinAmount = robot.getNumberOfCoins();
        final var expectedEndPosition = params.get("expectedEndPosition", Point.class);
        final var field = World.getGlobalWorld().getField(expectedEndPosition.x, expectedEndPosition.y);

        var paramsContext = params.toContext("robot", "expectedEndDirection");
        var cb = Assertions2.contextBuilder()
            .add(paramsContext)
            .add("robot (before call)", robot.toString())
            .add("robot (after call)", robot)
            .add("field", field);

        var context = cb.build();

        if (testStart) {
            Assertions3.assertImplementsInterfaceMethod(classLink, StudentLinks.MOVE_STRATEGY_START_LINK.get(), strict);
            Assertions4.assertElementsNotUsed(
                link.getCtElement(),
                context,
                Assertions4.buildCtElementBlacklist(
                    () -> Robot.class.getMethod("setField", int.class, int.class),
                    () -> Robot.class.getMethod("setX", int.class),
                    () -> Robot.class.getMethod("setY", int.class)
                )
            );
        }
        final var instance = spy(classLink.reflection());
        Assertions2.call(
            link,
            instance,
            context,
            r -> "The method threw an exception during execution.",
            robot,
            field
        );

        if (testStart) {
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
//            Assertions2.assertEquals(
//                expectedEndDirection,
//                robot.getDirection(),
//                context,
//                r -> "The robot direction changed."
//            );

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
        if (testGetMoveCount) {
            final var link2 = StudentLinks.MOVE_BY_WALK_GET_MOVE_COUNT_LINK.get();
            final var expectedMoveCount = ((MovementCountingRobot) robot).getMoveCount();
            Assertions3.assertImplementsInterfaceMethod(classLink, StudentLinks.MOVE_STRATEGY_WITH_COUNTER_GET_MOVE_COUNT_LINK.get());
            final var context2 = Assertions2.contextBuilder()
                .add(paramsContext)
                .add(context)
                .add("expectedMoveCount", expectedMoveCount)
                .build();

            final var result = Assertions2.callObject(
                link2,
                instance,
                context2,
                r -> "The method threw an exception during execution."
            );

            Assertions2.assertEquals(
                ((MovementCountingRobot) robot).getMoveCount(),
                result,
                context2,
                r -> "The method returned an incorrect move count."
            );
        }

    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H1_3.json", customConverters = "customConverters")
    public void testStartMethodNonStrict(final JsonParameterSet params) {
        testStartMethod(params, true, false, false);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H1_3.json", customConverters = "customConverters")
    public void testStartMethodStrict(final JsonParameterSet params) {
        testStartMethod(params, true, true, false);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "H1_3.json", customConverters = "customConverters")
    public void testGetMoveCount(final JsonParameterSet params) {
        testStartMethod(params, false, false, true);
    }
}
