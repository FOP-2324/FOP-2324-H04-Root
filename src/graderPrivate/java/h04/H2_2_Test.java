package h04;

import com.google.common.base.Suppliers;
import fopbot.Direction;
import fopbot.Field;
import fopbot.Robot;
import fopbot.World;
import h04.util.H04TestBase;
import h04.util.TestConstants;
import h04.util.reflect.Global;
import h04.util.reflect.StudentLinks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.match.BasicReflectionMatchers;
import org.tudalgo.algoutils.tutor.general.reflections.Modifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.mockito.Mockito.*;

@TestForSubmission
@Timeout(
    value = TestConstants.TEST_TIMEOUT_IN_SECONDS,
    unit = TimeUnit.SECONDS,
    threadMode = Timeout.ThreadMode.SEPARATE_THREAD
)
public class H2_2_Test extends H04TestBase {

    @Test
    public void testDeclaredCorrectly() {
        var link = StudentLinks.ROBOT_MOVER_LINK.get();
        Assertions.assertEquals(link.reflection().getName(), "h04.robot.RobotMover", "Wrong identifier.");
        Assertions3.assertCorrectModifiers(link, Modifier.PUBLIC, Modifier.CLASS, Modifier.NON_ABSTRACT);
        Assertions3.assertCorrectInterfaces(link, StudentLinks.FIELD_SELECTOR_LISTENER_LINK.get());
    }

    @Test
    public void testAddRobotDeclaration() {
        final var link = StudentLinks.ROBOT_MOVER_ADD_ROBOT_LINK.get();
        Assertions3.assertCorrectModifiers(link, Modifier.PUBLIC, Modifier.NON_ABSTRACT);
        Assertions3.assertCorrectReturnType(link, void.class);
        Assertions3.assertCorrectParameters(link, BasicReflectionMatchers.sameType(Robot.class));
    }

    /**
     * Tests if the addRobot method adds the robot to the robots array.
     *
     * @param usingLocalRobotsArray if true, the test will try to find the robots array in the RobotMover class. If
     *                              false, the test will rely on the onFieldSelection method to call the start method
     *                              of the move strategy for each added robot.
     */
    private void testAddRobotFunctionality(boolean usingLocalRobotsArray) {
        setupWorld(10, 10);
        final Supplier<Object> moveStrategy = Suppliers.memoize(
            () -> mock(StudentLinks.MOVE_STRATEGY_LINK.get().reflection())
        );
        Object instance = Global.createInstance(
            StudentLinks.ROBOT_MOVER_LINK.get(),
            StudentLinks.ROBOT_MOVER_CONSTRUCTOR_LINK::get,
            moveStrategy::get
        );
        final var robotsLink = StudentLinks.ROBOT_MOVER_ROBOTS_LINK.get();
        final var robotsList = List.of(
            new Robot(1, 1, Direction.UP, 10),
            new Robot(2, 2, Direction.DOWN, 20)
        );

        final var tmpRobotsList = new ArrayList<Robot>();
        final var field = World.getGlobalWorld().getField(5, 5);

        for (var robot : robotsList) {
            tmpRobotsList.add(robot);
            Assertions2.call(
                StudentLinks.ROBOT_MOVER_ADD_ROBOT_LINK.get(),
                instance,
                Assertions2.emptyContext(),
                r -> "an exception occurred while calling addRobot",
                robot
            );
            if (usingLocalRobotsArray) {
                Assertions2.assertTrue(
                    Global.arrayLikeToList(robotsLink.get(instance), Robot.class).containsAll(tmpRobotsList),
                    Assertions2.emptyContext(),
                    r -> "the robot was not added correctly"
                );
            } else {
                Assertions2.call(
                    StudentLinks.ROBOT_MOVER_ON_FIELD_SELECTION_LINK.get(),
                    instance,
                    Assertions2.emptyContext(),
                    r -> "an exception occurred while calling onFieldSelection",
                    field
                );
                for (var r2 : tmpRobotsList) {
                    Assertions2.call(() -> StudentLinks.MOVE_STRATEGY_START_LINK.get().invoke(
                        Mockito.verify(
                            moveStrategy.get(),
                            atLeastOnce()
                                .description("The start method of the move strategy was not called for the added robot"
                                                 + " when calling onFieldSelection().")
                        ),
                        same(r2),
                        any(Field.class)
                    ));
                }
                Mockito.reset(moveStrategy.get());
            }
        }
    }

    @Test
    public void testAddRobotFunctionality1() {
        testAddRobotFunctionality(true);
    }

    @Test
    public void testAddRobotFunctionality2() {
        testAddRobotFunctionality(false);
    }
}

