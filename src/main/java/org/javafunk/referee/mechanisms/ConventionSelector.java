package org.javafunk.referee.mechanisms;

import org.javafunk.referee.support.EnrichedClass;

public class ConventionSelector {
    public static Boolean appliesTo(EnrichedClass<?> enrichedClass) {
        return enrichedClass.findInnerClassWithName("Builder").hasValue();
    }
}
