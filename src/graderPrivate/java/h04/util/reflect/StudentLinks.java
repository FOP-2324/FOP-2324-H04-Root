package h04.util.reflect;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import fopbot.Robot;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.callable.ObjectCallable;
import org.tudalgo.algoutils.tutor.general.match.BasicReflectionMatchers;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.*;

import static h04.util.reflect.Global.similarityMatcher;

public class StudentLinks {
    public static final Supplier<PackageLink> PACKAGES_LINK = Suppliers.memoize(
        () -> new CombinedPackageLink(
            BasicPackageLink.of("h04"),
            BasicPackageLink.of("h04.robot"),
            BasicPackageLink.of("h04.selection"),
            BasicPackageLink.of("h04.strategy")
        )
    );

    private static Supplier<BasicTypeLink> typeLinkSup(String name) {
        return Suppliers.memoize(() -> (BasicTypeLink) Assertions3.assertTypeExists(
            PACKAGES_LINK.get(),
            similarityMatcher(name)
        ));
    }

    private static Supplier<BasicMethodLink> methodLinkSup(TypeLink tl, String name) {
        return Suppliers.memoize(() -> (BasicMethodLink) Assertions3.assertMethodExists(
            tl,
            similarityMatcher(name)
        ));
    }

    private static Supplier<BasicFieldLink> fieldLinkSup(TypeLink tl, Matcher<FieldLink> matcher) {
        return Suppliers.memoize(() -> (BasicFieldLink) Assertions3.assertFieldExists(
            tl,
            matcher
        ));
    }

    private static Supplier<BasicFieldLink> fieldLinkSup(TypeLink tl, String name) {
        return fieldLinkSup(tl, similarityMatcher(name));
    }

    public static Supplier<BasicConstructorLink> constructorLinkSup(TypeLink tl, Matcher<ConstructorLink> matcher) {
        return Suppliers.memoize(() -> (BasicConstructorLink) Assertions3.assertConstructorExists(
            tl,
            matcher
        ));
    }

    public static Supplier<BasicConstructorLink> constructorLinkSup(TypeLink tl, TypeLink... args) {
        return constructorLinkSup(tl, BasicReflectionMatchers.sameTypes(args));
    }

    public static Supplier<BasicConstructorLink> constructorLinkSup(TypeLink tl, ObjectCallable<Class<?>>... args) {
        return constructorLinkSup(tl, Arrays.stream(args)
            .map(l -> Assertions2.callObject(
                l,
                Assertions2.emptyContext(),
                r -> "The desired Class could not be retrieved. This is likely an Error in the tests."
            ))
            .map(BasicTypeLink::of)
            .toArray(TypeLink[]::new));
    }

    private static <T extends FieldLink> Matcher<T> arrayLikeMatcher(Class<?> clazz) {
        return Matcher.of(
            l -> {
                var typeClass = l.type().reflection();
                return (
                    // array
                    typeClass.isArray()
                        && typeClass.getComponentType().equals(clazz)
                ) || (
                    // collection
                    Collection.class.isAssignableFrom(typeClass)
                        && l.reflection().getGenericType() instanceof ParameterizedType pt
                        && pt.getActualTypeArguments()[0].equals(clazz)
                );
            },
            String.format("Field Type is array-like of %s", clazz.getSimpleName())
        );
    }

    // MoveStrategy
    public static final Supplier<BasicTypeLink> MOVE_STRATEGY_LINK = typeLinkSup("MoveStrategy");
    public static final Supplier<BasicMethodLink> MOVE_STRATEGY_START_LINK = methodLinkSup(
        MOVE_STRATEGY_LINK.get(),
        "start"
    );

    // MoveStrategyWithCounter
    public static final Supplier<BasicTypeLink> MOVE_STRATEGY_WITH_COUNTER_LINK = typeLinkSup("MoveStrategyWithCounter");
    public static final Supplier<BasicMethodLink> MOVE_STRATEGY_WITH_COUNTER_GET_MOVE_COUNT_LINK = methodLinkSup(
        MOVE_STRATEGY_WITH_COUNTER_LINK.get(),
        "getMoveCount"
    );

    // MoveByTeleport
    public static final Supplier<BasicTypeLink> MOVE_BY_TELEPORT_LINK = typeLinkSup("MoveByTeleport");
    public static final Supplier<BasicMethodLink> MOVE_BY_TELEPORT_START_LINK = methodLinkSup(
        MOVE_BY_TELEPORT_LINK.get(),
        "start"
    );

    // MoveByWalk
    public static final Supplier<BasicTypeLink> MOVE_BY_WALK_LINK = typeLinkSup("MoveByWalk");
    public static final Supplier<BasicMethodLink> MOVE_BY_WALK_START_LINK = methodLinkSup(
        MOVE_BY_WALK_LINK.get(),
        "start"
    );
    public static final Supplier<BasicMethodLink> MOVE_BY_WALK_GET_MOVE_COUNT_LINK = methodLinkSup(
        MOVE_BY_WALK_LINK.get(),
        "getMoveCount"
    );

    // FieldSelectorListener
    public static final Supplier<BasicTypeLink> FIELD_SELECTOR_LISTENER_LINK = typeLinkSup("FieldSelectionListener");
    public static final Supplier<BasicMethodLink> FIELD_SELECTOR_LISTENER_ON_FIELD_SELECTION_LINK = methodLinkSup(
        FIELD_SELECTOR_LISTENER_LINK.get(),
        "onFieldSelection"
    );

    // RobotMover
    public static final Supplier<BasicTypeLink> ROBOT_MOVER_LINK = typeLinkSup("RobotMover");
    public static final Supplier<BasicFieldLink> ROBOT_MOVER_ROBOTS_LINK = fieldLinkSup(
        ROBOT_MOVER_LINK.get(),
        arrayLikeMatcher(Robot.class)
    );
    public static final Supplier<BasicFieldLink> ROBOT_MOVER_MOVE_STRATEGY_LINK = fieldLinkSup(
        ROBOT_MOVER_LINK.get(),
        BasicReflectionMatchers.sameType(MOVE_STRATEGY_LINK.get())
    );
    public static final Supplier<BasicMethodLink> ROBOT_MOVER_ADD_ROBOT_LINK = methodLinkSup(
        ROBOT_MOVER_LINK.get(),
        "addRobot"
    );
    public static final Supplier<BasicMethodLink> ROBOT_MOVER_ON_FIELD_SELECTION_LINK = methodLinkSup(
        ROBOT_MOVER_LINK.get(),
        "onFieldSelection"
    );
    public static final Supplier<BasicConstructorLink> ROBOT_MOVER_CONSTRUCTOR_LINK = constructorLinkSup(
        ROBOT_MOVER_LINK.get(),
        MOVE_STRATEGY_LINK.get()
    );

    // MoveableRobot
    public static final Supplier<BasicTypeLink> MOVEABLE_ROBOT_LINK = typeLinkSup("MoveableRobot");
    public static final Supplier<BasicFieldLink> MOVEABLE_ROBOT_MOVE_STRATEGY_LINK = fieldLinkSup(
        MOVEABLE_ROBOT_LINK.get(),
        BasicReflectionMatchers.sameType(MOVE_STRATEGY_LINK.get())
    );
    public static final Supplier<BasicMethodLink> MOVEABLE_ROBOT_ADD_ROBOT_LINK = methodLinkSup(
        MOVEABLE_ROBOT_LINK.get(),
        "addRobot"
    );
    public static final Supplier<BasicMethodLink> MOVEABLE_ROBOT_ON_FIELD_SELECTION_LINK = methodLinkSup(
        MOVEABLE_ROBOT_LINK.get(),
        "onFieldSelection"
    );
    public static final Supplier<BasicConstructorLink> MOVEABLE_ROBOT_CONSTRUCTOR_LINK = constructorLinkSup(
        MOVEABLE_ROBOT_LINK.get(),
        MOVE_STRATEGY_LINK.get()
    );

    // FieldSelector
    public static final Supplier<BasicTypeLink> FIELD_SELECTOR_LINK = typeLinkSup("FieldSelector");
    public static final Supplier<BasicMethodLink> FIELD_SELECTOR_SET_FIELD_SELECTION_LISTENER_LINK = methodLinkSup(
        FIELD_SELECTOR_LINK.get(),
        "setFieldSelectionListener"
    );

    // MouseFieldSelector
    public static final Supplier<BasicTypeLink> MOUSE_FIELD_SELECTOR_LINK = typeLinkSup("MouseFieldSelector");
    public static final Supplier<BasicMethodLink> MOUSE_FIELD_SELECTOR_SET_FIELD_SELECTION_LISTENER_LINK = methodLinkSup(
        MOUSE_FIELD_SELECTOR_LINK.get(),
        "setFieldSelectionListener"
    );

    // KeyboardFieldSelector
    public static final Supplier<BasicTypeLink> KEYBOARD_FIELD_SELECTOR_LINK = typeLinkSup("KeyboardFieldSelector");
    public static final Supplier<BasicMethodLink> KEYBOARD_FIELD_SELECTOR_SET_FIELD_SELECTION_LISTENER_LINK = methodLinkSup(
        KEYBOARD_FIELD_SELECTOR_LINK.get(),
        "setFieldSelectionListener"
    );
}
