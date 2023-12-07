package h04;

import fopbot.Direction;
import fopbot.Robot;
import fopbot.World;
import h04.util.StudentLinks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions4;
import org.tudalgo.algoutils.tutor.general.reflections.Modifier;

import static org.mockito.Mockito.spy;

@TestForSubmission
public class H1_3_Test {

    @BeforeEach
    public void setup() {
        World.setSize(10, 10);
    }

    @Test
    public void testDeclaredCorrectly() {
        var link = StudentLinks.MOVE_BY_TELEPORT_LINK.get();
        Assertions.assertEquals(link.reflection().getName(), "h04.strategy.MoveByTeleport", "Wrong identifier.");
        Assertions3.assertCorrectModifiers(link, Modifier.PUBLIC, Modifier.CLASS, Modifier.NON_ABSTRACT);
        Assertions3.assertCorrectInterfaces(link, StudentLinks.MOVE_STRATEGY_LINK.get());
    }

    @Test
    public void testStartMethodDeclaredCorrectly() throws NoSuchMethodException {
        final var link = StudentLinks.MOVE_BY_TELEPORT_START_LINK.get();
        final var classLink = StudentLinks.MOVE_BY_TELEPORT_LINK.get();
        final var robot = new Robot(0, 0, Direction.DOWN, 0);
        var context = Assertions2.contextBuilder()
            .add("robot (before call)", robot.toString())
            .add("robot (after call)", robot)
            .add("expectedEndPosition", String.format("(%d, %d)", 5, 5))
            .build();
        Assertions3.assertImplementsInterfaceMethod(classLink, StudentLinks.MOVE_STRATEGY_START_LINK.get());
        Assertions4.assertElementsNotUsed(
            link.getCtElement(),
            context,
            Assertions4.buildCtElementBlacklist(
                Robot.class.getMethod("move")
            )
        );
        final var instance = spy(classLink.reflection());
        Assertions2.call(
            link,
            instance,
            context,
            r -> "The method threw an exception during execution.",
            robot,
            World.getGlobalWorld().getField(5, 5)
        );

        Assertions2.assertEquals(
            5,
            robot.getX(),
            context,
            r -> "The robot did not end up at the expected x position."
        );

        Assertions2.assertEquals(
            5,
            robot.getY(),
            context,
            r -> "The robot did not end up at the expected y position."
        );
    }
}
