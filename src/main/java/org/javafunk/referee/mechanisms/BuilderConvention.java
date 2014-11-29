package org.javafunk.referee.mechanisms;

import org.javafunk.funk.monads.Option;
import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.support.EnrichedMethod;

public interface BuilderConvention {
    Option<EnrichedMethod> witherFor(String attributeName);
    Option<EnrichedClass<?>> typeFor(String attributeName);
    Option<EnrichedMethod> builder();
    Object instance();
}
