package org.javafunk.referee;

import lombok.Value;

@Value
public class PopulationResult<T> {
    T instance;
    ProblemReport problemReport;
}
