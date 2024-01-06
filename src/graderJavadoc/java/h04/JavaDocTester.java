package h04;

import com.google.common.reflect.ClassPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.testing.RuntimeClassLoader;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;
import org.tudalgo.algoutils.tutor.general.ResourceUtils;
import org.tudalgo.algoutils.tutor.general.Utils;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A Class that verifies every JavaDoc comment in the project is present. We keep track of the amount of JavaDoc comments
 * that are missing fields or are missing entirely.
 */
public class JavaDocTester {
    public static AtomicInteger missingJavaDocAmount = new AtomicInteger(0);

    public static final String rootPkg = "h04";

    public static Class<?>[] getStudentClasses() {
        if (Utils.isJagrRun()) {
            //noinspection UnstableApiUsage
            final var cycle = TestCycleResolver.getTestCycle();
            RuntimeClassLoader classLoader = cycle.getClassLoader();
            return classLoader.getClassNames().stream()
                .filter(name -> cycle.getSubmission().getSourceFile(ResourceUtils.toPathString(name)) != null)
                .map(classLoader::loadClass)
                .filter(c -> !c.getName().contains("$") || c.getDeclaringClass() != null)
                .toArray(Class<?>[]::new);
        } else {
            var cl = Thread.currentThread().getContextClassLoader();
            var cp = Assertions.assertDoesNotThrow(() -> ClassPath.from(cl));
            System.out.println(cp.getResources().stream().filter(x -> x.getResourceName().contains("h04")).map(x -> x.getResourceName()).toList());
            return cp.getTopLevelClassesRecursive(rootPkg).stream()
                // only main sourceset
//                .peek(c -> System.out.println(c.getName()))
                .map(ClassPath.ClassInfo::load).toArray(Class<?>[]::new);
        }
    }

    @Test
    public void testJDocStuff() {
        var classes = getStudentClasses();
        System.out.println(Arrays.toString(classes));
    }
}
