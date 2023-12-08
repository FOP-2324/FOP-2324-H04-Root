package h04.util;

import com.fasterxml.jackson.databind.JsonNode;
import fopbot.World;
import h04.util.json.JsonConverters;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;

import java.util.Map;
import java.util.function.Function;

import static h04.util.TestConstants.SHOW_WORLD;
import static h04.util.TestConstants.WORLD_DELAY;

/**
 * This class is the common base class for all tests of this exercise.
 */
@SkipAfterFirstFailedTest(TestConstants.SKIP_AFTER_FIRST_FAILED_TEST)
public abstract class H04TestBase {
    /**
     * The custom converters for this test class.
     */
    @SuppressWarnings("unused")
    public static final Map<String, Function<JsonNode, ?>> customConverters = Map.ofEntries(
        Map.entry("worldWidth", JsonNode::asInt),
        Map.entry("worldHeight", JsonNode::asInt),
        Map.entry("walls", n -> JsonConverters.toList(n, JsonConverters::toDirection)),
        Map.entry("robot", JsonConverters::toRobot),
        Map.entry("expectedEndPosition", JsonConverters::toPoint),
        Map.entry("expectedEndDirection", JsonConverters::toDirection)
    );

    /**
     * Creates a world with the given width and height and sets the GameConstants accordingly.
     * @param worldWidth The width of the world.
     * @param worldHeight The height of the world.
     */
    public static void setupWorld(final int worldWidth, final int worldHeight) {
        World.setSize(worldWidth, worldHeight);
        //noinspection UnstableApiUsage
        World.getGlobalWorld().setActionLimit(1024);
        if (SHOW_WORLD) {
            World.setDelay(WORLD_DELAY);
            World.setVisible(true);
        } else {
            World.setDelay(0);
        }
    }
}
