package h04;

import com.google.common.base.Suppliers;
import fopbot.Direction;
import fopbot.Field;
import fopbot.Robot;
import fopbot.World;
import h04.util.H04TestBase;
import h04.util.reflect.Global;
import h04.util.reflect.StudentLinks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.opentest4j.AssertionFailedError;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.match.BasicReflectionMatchers;
import org.tudalgo.algoutils.tutor.general.reflections.FieldLink;
import org.tudalgo.algoutils.tutor.general.reflections.Modifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static org.mockito.Mockito.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
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
        FieldLink robotsLink;
        boolean robotsIsList = false;
        try {
            robotsLink = StudentLinks.ROBOT_MOVER_ROBOTS_ARRAY_LINK.get();
            assertTrue(robotsLink.type().reflection().isArray(), emptyContext(), result -> "");
        } catch (AssertionFailedError e) {
            robotsLink = StudentLinks.ROBOT_MOVER_ROBOTS_LIST_LINK.get();
            robotsIsList = true;
        }
        final var robotsList = List.of(
            new Robot(1, 1, Direction.UP, 10),
            new Robot(2, 2, Direction.DOWN, 20)
        );

        final var tmpRobotsList = new ArrayList<Robot>();
        final var field = World.getGlobalWorld().getField(5, 5);

        for (var robot : robotsList) {
            tmpRobotsList.add(robot);
            call(
                StudentLinks.ROBOT_MOVER_ADD_ROBOT_LINK.get(),
                instance,
                Assertions2.emptyContext(),
                r -> "an exception occurred while calling addRobot",
                robot
            );
            if (usingLocalRobotsArray) {
                Assertions2.assertTrue(
                    (!robotsIsList ? Global.arrayLikeToList(robotsLink.get(instance), Robot.class) : robotsLink.<List<Robot>>get(instance))
                        .containsAll(tmpRobotsList),
                    Assertions2.emptyContext(),
                    r -> "the robot was not added correctly"
                );
            } else {
                call(
                    StudentLinks.ROBOT_MOVER_ON_FIELD_SELECTION_LINK.get(),
                    instance,
                    Assertions2.emptyContext(),
                    r -> "an exception occurred while calling onFieldSelection",
                    field
                );
                for (var r2 : tmpRobotsList) {
                    call(() -> StudentLinks.MOVE_STRATEGY_START_LINK.get().invoke(
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

    @Test
    public void testOnFieldSelection() throws ReflectiveOperationException {
        int worldWidth = 10;
        int worldHeight = 10;
        setupWorld(worldWidth, worldHeight);
        var link = StudentLinks.ROBOT_MOVER_LINK.get();
        var moveStrategyFieldLink = StudentLinks.ROBOT_MOVER_MOVE_STRATEGY_LINK.get();
        FieldLink robotsLink;
        boolean robotsIsList = false;
        try {
            robotsLink = StudentLinks.ROBOT_MOVER_ROBOTS_ARRAY_LINK.get();
            assertTrue(robotsLink.type().reflection().isArray(), emptyContext(), result -> "");
        } catch (AssertionFailedError e) {
            robotsLink = StudentLinks.ROBOT_MOVER_ROBOTS_LIST_LINK.get();
            robotsIsList = true;
        }
        var onFieldSelectionLink = StudentLinks.ROBOT_MOVER_ON_FIELD_SELECTION_LINK.get();

        var passedRobotsRefs = new ArrayList<Robot>();
        var passedFieldRefs = new ArrayList<Field>();
        var moveStrategyInstance = mock(StudentLinks.MOVE_STRATEGY_LINK.get().reflection(), (Answer<Object>) invocationOnMock -> {
            if (invocationOnMock.getMethod().equals(StudentLinks.MOVE_STRATEGY_START_LINK.get().reflection())) {
                passedRobotsRefs.add(invocationOnMock.getArgument(0));
                passedFieldRefs.add(invocationOnMock.getArgument(1));
                return null;
            } else {
                return invocationOnMock.callRealMethod();
            }
        });

        var random = new Random();
        var robots = new ArrayList<Robot>();
        for (int i = 0; i < 5; i++) {
            robots.add(new Robot(random.nextInt(worldWidth), random.nextInt(worldHeight)));
        }

        var instance = mock(link.reflection(), CALLS_REAL_METHODS);
        moveStrategyFieldLink.set(instance, moveStrategyInstance);
        if (robotsIsList) {
            if (robotsLink.type().reflection() != List.class) {
                robotsLink.set(instance, robotsLink.reflection().getType().getConstructor(Collection.class).newInstance(robots));
            } else {
                robotsLink.set(instance, robots);
            }
        } else {
            robotsLink.set(instance, robots.toArray(Robot[]::new));
        }

        var field = new Field(World.getGlobalWorld(), 4, 2);
        var context = contextBuilder()
            .add("moveStrategy", moveStrategyInstance)
            .add("robots", robotsLink.get(instance))
            .add("field", "fopbot.Field[x=%d, y=%d]".formatted(field.getX(), field.getY()))
            .build();
        call(() -> onFieldSelectionLink.invoke(instance, field), context, result ->
            "An exception occurred while invoking RobotMover.onFieldSelection(Field)");
        assertEquals(robots.size(), passedRobotsRefs.size(), context, result ->
            "MoveStrategy.start(Robot, Field) was not called the expected number of times");
        assertTrue(passedRobotsRefs.containsAll(robots), context, result ->
            "MoveStrategy.start(Robot, Field) was not called with each robots exactly once");
        assertTrue(passedFieldRefs.stream().allMatch(fieldRef -> fieldRef == field), context, result ->
            "MoveStrategy.start(Robot, Field) was called with an unexpected field");
    }

    @Test
    public void testConstructor() {
        var moveStrategyFieldLink = StudentLinks.ROBOT_MOVER_MOVE_STRATEGY_LINK.get();
        var constructorLink = StudentLinks.ROBOT_MOVER_CONSTRUCTOR_LINK.get();
        var moveStrategyInstance = mock(StudentLinks.MOVE_STRATEGY_LINK.get().reflection());

        var context = contextBuilder()
            .add("moveStrategy", moveStrategyInstance)
            .build();
        var instance = callObject(() -> constructorLink.invoke(moveStrategyInstance), context, result ->
            "An exception occurred while invoking the constructor of RobotMover");
        assertSame(moveStrategyInstance, moveStrategyFieldLink.get(instance), context, result ->
            "The constructor of RobotMover did not set field %s correctly".formatted(moveStrategyFieldLink.identifier()));
    }
}
