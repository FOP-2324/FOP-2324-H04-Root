package h04.util.json;

import com.fasterxml.jackson.databind.JsonNode;
import fopbot.Direction;
import fopbot.Robot;
import fopbot.RobotFamily;

import java.awt.Point;

/**
 * Custom JSON converters for the {@link h04} exercise.
 */
public class JsonConverters extends org.tudalgo.algoutils.tutor.general.json.JsonConverters {
    /**
     * Converts a JSON node to a {@link Point}.
     *
     * @param jsonNode The JSON node to convert.
     * @return The converted {@link Point}.
     */
    public static Point toPoint(final JsonNode jsonNode) {
        return new Point(
            jsonNode.get("x").asInt(),
            jsonNode.get("y").asInt()
        );
    }

    /**
     * Converts a JSON node to a {@link Robot}.
     *
     * @param jsonNode The JSON node to convert.
     * @return The converted {@link Robot}.
     */
    public static Robot toRobot(final JsonNode jsonNode) {
        var family = RobotFamily.TRIANGLE_BLUE;
        if (jsonNode.has("family")) {
            family = RobotFamily.valueOf(jsonNode.get("family").asText());
        }
        return new Robot(
            jsonNode.get("x").asInt(0),
            jsonNode.get("y").asInt(0),
            toDirection(jsonNode.get("direction")),
            jsonNode.get("numberOfCoins").asInt(10),
            family
        );
    }

    /**
     * Converts a JSON node to a {@link Direction}.
     *
     * @param jsonNode The JSON node to convert.
     * @return The converted {@link Direction}.
     */
    public static Direction toDirection(final JsonNode jsonNode) {
        return Direction.valueOf(jsonNode.asText());
    }
}
