package h04.util;

import org.tudalgo.algoutils.tutor.general.match.Match;
import org.tudalgo.algoutils.tutor.general.match.Matcher;

public class SimilarityMatcher<T> implements Matcher<T> {
    @Override
    public Object object() {
        return Matcher.super.object();
    }

    @Override
    public <ST extends T> Match<ST> match(ST object) {
        return null;
    }
}
