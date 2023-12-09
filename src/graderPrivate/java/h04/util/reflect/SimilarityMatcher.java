package h04.util.reflect;

import org.tudalgo.algoutils.tutor.general.match.Match;
import org.tudalgo.algoutils.tutor.general.match.Matcher;

public class SimilarityMatcher<T> implements Matcher<T> {
    @Override
    public String characteristic() {
        return Matcher.super.characteristic();
    }

    @Override
    public <ST extends T> Match<ST> match(ST object) {
        return null;
    }
}
