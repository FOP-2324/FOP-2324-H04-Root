package h04.util;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions3;
import org.tudalgo.algoutils.tutor.general.reflections.*;

import static h04.util.Global.similarityMatcher;

public class StudentLinks {
    public static final Supplier<PackageLink> PACKAGES_LINK = Suppliers.memoize(
        () -> new CombinedPackageLink(
            BasicPackageLink.of("h04"),
            BasicPackageLink.of("h04.robots"),
            BasicPackageLink.of("h04.selection"),
            BasicPackageLink.of("h04.strategy")
        )
    );

    private static Supplier<TypeLink> typeLinkSup(String name) {
        return Suppliers.memoize(() -> Assertions3.assertTypeExists(
            PACKAGES_LINK.get(),
            similarityMatcher(name)
        ));
    }

    private static Supplier<MethodLink> methodLinkSup(TypeLink tl, String name) {
        return Suppliers.memoize(() -> Assertions3.assertMethodExists(
            tl,
            similarityMatcher(name)
        ));
    }

    private static Supplier<FieldLink> fieldLinkSup(TypeLink tl, String name) {
        return Suppliers.memoize(() -> Assertions3.assertFieldExists(
            tl,
            similarityMatcher(name)
        ));
    }

    public static final Supplier<TypeLink> MOVE_STRATEGY_LINK = typeLinkSup("MoveStrategy");
    public static final Supplier<MethodLink> MOVE_STRATEGY_START_LINK = methodLinkSup(
        MOVE_STRATEGY_LINK.get(),
        "start"
    );

    public static final Supplier<TypeLink> MOVE_STRATEGY_WITH_COUNTER_LINK = typeLinkSup("MoveStrategyWithCounter");
    public static final Supplier<MethodLink> MOVE_STRATEGY_WITH_COUNTER_GET_MOVE_COUNT_LINK = methodLinkSup(
        MOVE_STRATEGY_WITH_COUNTER_LINK.get(),
        "getMoveCount"
    );
}
