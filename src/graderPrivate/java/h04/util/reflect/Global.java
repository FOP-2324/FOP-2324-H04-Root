package h04.util.reflect;

import fopbot.Direction;
import fopbot.Robot;
import fopbot.RobotFamily;
import org.junit.jupiter.api.Assertions;
import org.tudalgo.algoutils.tutor.general.match.Match;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.match.MatchingUtils;
import org.tudalgo.algoutils.tutor.general.match.Stringifiable;
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.BasicPackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.PackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;
import org.tudalgo.algoutils.tutor.general.stringify.HTML;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static org.tudalgo.algoutils.tutor.general.stringify.HTML.it;

/**
 * Global constants and methods.
 */
@SuppressWarnings({"deprecated", "unused"})
public class Global {

    /**
     * The minimum similarity between two strings to be considered equal.
     */
    private static final double MINIMUM_SIMILARITY = .8;

    /**
     * The {@link PackageLink} of the {@code h03} package.
     */
    public static final PackageLink H04_LINK = BasicPackageLink.of("h04");

    /**
     * The {@link TypeLink} of the {@link Object} class.
     */
    public static final TypeLink OBJECT_LINK = BasicTypeLink.of(Object.class);

    /**
     * The {@link TypeLink} of the {@link Direction} class.
     */
    public static final TypeLink DIRECTION_LINK = BasicTypeLink.of(Direction.class);

    /**
     * The {@link TypeLink} of the {@code void} class.
     */
    public static final TypeLink VOID_LINK = BasicTypeLink.of(void.class);

    /**
     * The {@link TypeLink} of the {@code int} class.
     */
    public static final TypeLink INT_LINK = BasicTypeLink.of(int.class);

    /**
     * The {@link TypeLink} of the {@code boolean} class.
     */
    public static final TypeLink BOOLEAN_LINK = BasicTypeLink.of(boolean.class);

    /**
     * The {@link MethodLink} of the {@link Robot#move()} method.
     */
    public static final MethodLink ROBOT_MOVE = BasicMethodLink.of(
        Assertions.assertDoesNotThrow(() -> Robot.class.getMethod("move"))
    );

    /**
     * The {@link TypeLink} of the {@link Robot} class.
     */
    public static final TypeLink ROBOT_LINK = BasicTypeLink.of(Robot.class);

    /**
     * The {@link TypeLink} of the {@link Robot} array class.
     */
    public static final TypeLink ROBOT_ARRAY_LINK = BasicTypeLink.of(Robot[].class);

    /**
     * The {@link TypeLink} of the {@link RobotFamily} class.
     */
    public static final TypeLink ROBOT_FAMILY_LINK = BasicTypeLink.of(RobotFamily.class);

    /**
     * The {@link TypeLink} of the {@link RobotFamily} array class.
     */
    public static final TypeLink ROBOT_FAMILY_ARRAY_LINK = BasicTypeLink.of(RobotFamily[].class);

    /**
     * The amount of misspelled names in the student implementation.
     */
    public static int MISSPELLING_COUNTER = 0;

    /**
     * The misspelled names in the student implementation.
     */
    public static final List<String> MISSPELLINGS = new ArrayList<>();

    /**
     * Returns an Error message, if the student implementation contains misspelled names.
     *
     * @return an Error message, if the student implementation contains misspelled names
     */
    public static String misspellings() {
        if (MISSPELLING_COUNTER == 0) {
            return "";
        }
        return it("The following names are misspelled in your solution:\n")
            + MISSPELLINGS.stream().sorted().map(HTML::tt).collect(Collectors.joining("\n")
        );
    }

    /**
     * Returns a {@link Matcher} for the given string.
     *
     * @param string the string to match
     * @param <T>    the type of the object to match
     * @return a {@link Matcher} for the given string
     */
    public static <T extends Stringifiable> Matcher<T> similarityMatcher(final String string) {
        return new Matcher<>() {

            /**
             * The maximum similarity between the given string and a matched object.
             */
            double maxSimilarity = 0;

            @Override
            public Object object() {
                return string;
            }

            @Override
            public <ST extends T> Match<ST> match(final ST object) {

                return new Match<>() {

                    final double similarity = MatchingUtils.similarity(object.string(), string);

                    {
                        if (matched()) {
                            if (maxSimilarity == 0 && similarity != 1) {
                                MISSPELLING_COUNTER++;
                                MISSPELLINGS.add(string);
                            } else if (maxSimilarity != 0 && maxSimilarity != 1 && similarity == 1) {
                                MISSPELLING_COUNTER--;
                                MISSPELLINGS.remove(string);
                            }
                            maxSimilarity = max(maxSimilarity, similarity);
                        }
                    }

                    @Override
                    public boolean matched() {
                        return similarity >= MINIMUM_SIMILARITY;
                    }

                    @Override
                    public ST object() {
                        return object;
                    }

                    @Override
                    public int compareTo(final Match<ST> other) {
                        if (!other.matched()) {
                            return matched() ? 1 : 0;
                        } else if (!matched()) {
                            return -1;
                        }
                        final double otherSimilarity = MatchingUtils.similarity(other.object().string(), string);
                        return Double.compare(similarity, otherSimilarity);
                    }
                };
            }
        };
    }
}
