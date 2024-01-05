package h04;

import h04.util.H04TestBase;
import h04.util.reflect.StudentLinks;
import org.junit.jupiter.api.Test;
import org.mockito.MockSettings;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class H4_Test extends H04TestBase {

    @Test
    public void testMain01() throws Throwable {
        setupWorld(10, 10);
        var mouseFieldSelectorLink = StudentLinks.MOUSE_FIELD_SELECTOR_LINK.get();
        var moveByWalkLink = StudentLinks.MOVE_BY_WALK_LINK.get();
        var robotMoverLink = StudentLinks.ROBOT_MOVER_LINK.get();
        var moveStrategyFieldLink = StudentLinks.ROBOT_MOVER_MOVE_STRATEGY_LINK.get();

        var fieldSelectionListenerRef = new AtomicReference<>();
        Answer<?> mfsSetterAnswer = invocationOnMock -> {
            if (invocationOnMock.getMethod().equals(StudentLinks.MOUSE_FIELD_SELECTOR_SET_FIELD_SELECTION_LISTENER_LINK.get().reflection())) {
                fieldSelectionListenerRef.set(invocationOnMock.getArgument(0));
                return null;
            } else {
                return invocationOnMock.callRealMethod();
            }
        };
        var moveByWalkRef = new AtomicReference<>();
        var robotRefs = new ArrayList<>();
        MockedConstruction.MockInitializer rmInitializer = (mock, context) -> {
            moveByWalkRef.set(context.arguments().get(0));
            moveStrategyFieldLink.set(mock, context.arguments().get(0));
        };
        MockSettings rmMockSettings = new MockSettingsImpl<>().defaultAnswer(invocationOnMock -> {
            if (invocationOnMock.getMethod().equals(StudentLinks.ROBOT_MOVER_ADD_ROBOT_LINK.get().reflection())) {
                robotRefs.add(invocationOnMock.getArgument(0));
                return null;
            } else {
                return invocationOnMock.callRealMethod();
            }
        });

        try (var ignored = Mockito.mockConstructionWithAnswer(mouseFieldSelectorLink.reflection(), mfsSetterAnswer);
             var ignored1 = Mockito.mockConstructionWithAnswer(moveByWalkLink.reflection(), InvocationOnMock::callRealMethod);
             var ignored2 = Mockito.mockConstruction(robotMoverLink.reflection(), rmMockSettings, rmInitializer)) {
            Main.main01();

            assertNotNull(moveByWalkRef.get(), emptyContext(), result ->
                "No instance of MoveByWalk was passed to the constructor of RobotMover in main01()");
            assertNotNull(fieldSelectionListenerRef.get(), emptyContext(), result ->
                "No instance of RobotMover was set as listener in a MouseFieldSelector instance in main01()");
            assertEquals(3, robotRefs.size(), emptyContext(), result ->
                "RobotMover.addRobot(Robot) was not called exactly 3 times");
        } catch (Throwable t) {
            fail(t, emptyContext(), result -> "An exception occurred while grading main01()");
        }
    }

    @Test
    public void testMain02() {
        setupWorld(10, 10);
        var keyboardFieldSelectorLink = StudentLinks.KEYBOARD_FIELD_SELECTOR_LINK.get();
        var moveByTeleportLink = StudentLinks.MOVE_BY_TELEPORT_LINK.get();
        var moveableRobotLink = StudentLinks.MOVEABLE_ROBOT_LINK.get();
        var moveStrategyFieldLink = StudentLinks.MOVEABLE_ROBOT_MOVE_STRATEGY_LINK.get();

        var fieldSelectionListenerRef = new AtomicReference<>();
        Answer<?> kfsSetterAnswer = invocationOnMock -> {
            if (invocationOnMock.getMethod().equals(StudentLinks.KEYBOARD_FIELD_SELECTOR_SET_FIELD_SELECTION_LISTENER_LINK.get().reflection())) {
                fieldSelectionListenerRef.set(invocationOnMock.getArgument(0));
                return null;
            } else {
                return invocationOnMock.callRealMethod();
            }
        };
        var moveByTeleportRef = new AtomicReference<>();
        MockedConstruction.MockInitializer mrInitializer = (mock, context) -> {
            moveByTeleportRef.set(context.arguments().get(0));
            moveStrategyFieldLink.set(mock, context.arguments().get(0));
        };

        try (var ignored = Mockito.mockConstructionWithAnswer(keyboardFieldSelectorLink.reflection(), kfsSetterAnswer);
             var ignored1 = Mockito.mockConstructionWithAnswer(moveByTeleportLink.reflection(), InvocationOnMock::callRealMethod);
             var ignored2 = Mockito.mockConstruction(moveableRobotLink.reflection(), mrInitializer)) {
            Main.main02();

            assertNotNull(moveByTeleportRef.get(), emptyContext(), result ->
                "No instance of MoveByTeleport was passed to the constructor of MoveableRobot in main02()");
            assertNotNull(fieldSelectionListenerRef.get(), emptyContext(), result ->
                "No instance of MoveableRobot was set as listener in a KeyboardFieldSelector instance in main02()");
        } catch (Throwable t) {
            fail(t, emptyContext(), result -> "An exception occurred while grading main02()");
        }
    }
}
