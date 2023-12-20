package h04;

import fopbot.Field;
import fopbot.Robot;
import fopbot.World;
import h04.util.H04TestBase;
import h04.util.TestConstants;
import h04.util.reflect.StudentLinks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.stubbing.Answer;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.Modifier;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.Mockito.mock;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
@Timeout(
    value = TestConstants.TEST_TIMEOUT_IN_SECONDS,
    unit = TimeUnit.SECONDS,
    threadMode = Timeout.ThreadMode.SEPARATE_THREAD
)
public class H2_3_Test extends H04TestBase {

    @Test
    public void testDeclaredCorrectly() {
        var link = StudentLinks.MOVEABLE_ROBOT_LINK.get();
        Assertions.assertEquals(link.reflection().getName(), "h04.robot.MoveableRobot", "Wrong identifier.");
        Assertions3.assertCorrectModifiers(link, Modifier.PUBLIC, Modifier.CLASS, Modifier.NON_ABSTRACT);
        Assertions3.assertCorrectSuperType(link, Robot.class);
        Assertions3.assertCorrectInterfaces(link, StudentLinks.FIELD_SELECTOR_LISTENER_LINK.get());
    }

    @Test
    public void testConstructor() {
        setupWorld(5, 5);
        var constructorLink = StudentLinks.MOVEABLE_ROBOT_CONSTRUCTOR_LINK.get();
        Assertions3.assertCorrectModifiers(constructorLink, Modifier.PUBLIC);
        Assertions3.assertCorrectParameters(constructorLink,
            Matcher.of(typeLink -> typeLink.reflection() == StudentLinks.MOVE_STRATEGY_LINK.get().reflection()));

        var moveStrategyFieldLink = StudentLinks.MOVEABLE_ROBOT_MOVE_STRATEGY_LINK.get();
        var moveStrategyInstance = mock(StudentLinks.MOVE_STRATEGY_LINK.get().reflection());
        var context = contextBuilder()
            .add("moveStrategy", moveStrategyInstance)
            .build();

        var instance = callObject(() -> constructorLink.invoke(moveStrategyInstance), context, result ->
            "An exception occurred while invoking the constructor of MoveableRobot");
        assertTrue(World.getGlobalWorld().getField(0, 0).getEntities().stream().anyMatch(fieldEntity -> fieldEntity == instance), context, result ->
            "The super constructor was called with the correct coordinates");
        assertSame(moveStrategyInstance, moveStrategyFieldLink.get(instance), context, result ->
            "The constructor of MoveableRobot did not set field %s correctly".formatted(moveStrategyFieldLink.identifier()));
    }

    private void testOnFieldSelection(boolean isSpinnyBoi) {
        int worldWidth = 5;
        int worldHeight = 5;
        int expectedNumberOfTurns = 5;
        setupWorld(worldWidth, worldHeight);
        var link = StudentLinks.MOVEABLE_ROBOT_LINK.get();
        var moveStrategyFieldLink = StudentLinks.MOVEABLE_ROBOT_MOVE_STRATEGY_LINK.get();
        var onFieldSelectionLink = StudentLinks.MOVEABLE_ROBOT_ON_FIELD_SELECTION_LINK.get();

        var passedRobotsRef = new AtomicReference<Robot>();
        var passedFieldRefs = new ArrayList<Field>();
        var moveStrategyInstance = mock((isSpinnyBoi ? StudentLinks.MOVE_STRATEGY_WITH_COUNTER_LINK : StudentLinks.MOVE_STRATEGY_LINK).get().reflection(),
            (Answer<Object>) invocationOnMock -> {
                if (invocationOnMock.getMethod().equals(StudentLinks.MOVE_STRATEGY_START_LINK.get().reflection())) {
                    passedRobotsRef.set(invocationOnMock.getArgument(0));
                    passedFieldRefs.add(invocationOnMock.getArgument(1));
                    return null;
                } else if (invocationOnMock.getMethod().equals(StudentLinks.MOVE_STRATEGY_WITH_COUNTER_GET_MOVE_COUNT_LINK.get().reflection())) {
                    return expectedNumberOfTurns;
                } else {
                    return invocationOnMock.callRealMethod();
                }
            });

        var numberOfTurns = new AtomicInteger(0);
        var instance = mock(link.reflection(), (Answer<Object>) invocationOnMock -> {
            if (invocationOnMock.getMethod().equals(Robot.class.getMethod("turnLeft"))) {
                numberOfTurns.incrementAndGet();
                return null;
            } else {
                return invocationOnMock.callRealMethod();
            }
        });
        moveStrategyFieldLink.set(instance, moveStrategyInstance);

        var field = new Field(World.getGlobalWorld(), 4, 2);
        var context = contextBuilder()
            .add("moveStrategy", moveStrategyInstance)
            .add("field", "fopbot.Field[x=%d, y=%d]".formatted(field.getX(), field.getY()))
            .build();
        call(() -> onFieldSelectionLink.invoke(instance, field), context, result ->
            "An exception occurred while invoking MoveableRobot.onFieldSelection(Field)");
        assertSame(instance, passedRobotsRef.get(), context, result ->
            "MoveStrategy.start(Robot, Field) was not called with each robots exactly once");
        assertTrue(passedFieldRefs.stream().allMatch(fieldRef -> fieldRef == field), context, result ->
            "MoveStrategy.start(Robot, Field) was called with an unexpected field");
        if (isSpinnyBoi) {
            assertEquals(expectedNumberOfTurns, numberOfTurns.get(), context, result ->
                "The robot did not spin the expected number of times");
        }
    }

    @Test
    public void testOnFieldSelectionNormal() {
        testOnFieldSelection(false);
    }

    @Test
    public void testOnFieldSelectionAlt() {
        testOnFieldSelection(true);
    }
}
