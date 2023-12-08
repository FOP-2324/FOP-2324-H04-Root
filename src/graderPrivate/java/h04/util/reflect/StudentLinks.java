package h04.util.reflect;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.reflections.*;

import static h04.util.reflect.Global.similarityMatcher;

public class StudentLinks {
    public static final Supplier<PackageLink> PACKAGES_LINK = Suppliers.memoize(
        () -> new CombinedPackageLink(
            BasicPackageLink.of("h04"),
            BasicPackageLink.of("h04.robots"),
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

    private static Supplier<BasicFieldLink> fieldLinkSup(TypeLink tl, String name) {
        return Suppliers.memoize(() -> (BasicFieldLink) Assertions3.assertFieldExists(
            tl,
            similarityMatcher(name)
        ));
    }

    public static final Supplier<BasicTypeLink> MOVE_STRATEGY_LINK = typeLinkSup("MoveStrategy");
    public static final Supplier<BasicMethodLink> MOVE_STRATEGY_START_LINK = methodLinkSup(
        MOVE_STRATEGY_LINK.get(),
        "start"
    );

    public static final Supplier<BasicTypeLink> MOVE_STRATEGY_WITH_COUNTER_LINK = typeLinkSup("MoveStrategyWithCounter");
    public static final Supplier<BasicMethodLink> MOVE_STRATEGY_WITH_COUNTER_GET_MOVE_COUNT_LINK = methodLinkSup(
        MOVE_STRATEGY_WITH_COUNTER_LINK.get(),
        "getMoveCount"
    );

    public static final Supplier<BasicTypeLink> MOVE_BY_TELEPORT_LINK = typeLinkSup("MoveByTeleport");
    public static final Supplier<BasicMethodLink> MOVE_BY_TELEPORT_START_LINK = methodLinkSup(
        MOVE_BY_TELEPORT_LINK.get(),
        "start"
    );

    public static final Supplier<BasicTypeLink> MOVE_BY_WALK_LINK = typeLinkSup("MoveByWalk");
    public static final Supplier<BasicMethodLink> MOVE_BY_WALK_START_LINK = methodLinkSup(
        MOVE_BY_WALK_LINK.get(),
        "start"
    );

    public static final Supplier<BasicMethodLink> MOVE_BY_WALK_GET_MOVE_COUNT_LINK = methodLinkSup(
        MOVE_BY_WALK_LINK.get(),
        "getMoveCount"
    );
}
