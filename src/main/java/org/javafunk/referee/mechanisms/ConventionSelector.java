package org.javafunk.referee.mechanisms;

import org.javafunk.funk.functors.Mapper;
import org.javafunk.funk.monads.Option;
import org.javafunk.referee.support.EnrichedClass;

public class ConventionSelector {
    public static Boolean appliesTo(EnrichedClass<?> enrichedClass) {
        System.out.println(enrichedClass.getUnderlyingClass());
        return enrichedClass.findInnerClassWithName("Builder").hasValue();
    }
}
