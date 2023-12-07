package h04.util;

import com.google.common.base.Supplier;
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

    public static class MoveStrategy_Student {
        public static final Supplier<TypeLink> MOVE_STRATEGY_LINK = Suppliers.memoize(
            () -> Assertions3.assertTypeExists(
                PACKAGES_LINK.get(),
                similarityMatcher("MoveStrategy")
            )
        );

        public static final Supplier<MethodLink> MOVE_STRATEGY_START_LINK = Suppliers.memoize(
            () -> Assertions3.assertMethodExists(
                MOVE_STRATEGY_LINK.get(),
                similarityMatcher("start")
            )
        );
    }
}
