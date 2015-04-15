package org.javafunk.referee;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.javafunk.referee.mechanisms.PopulationMechanism;
import org.javafunk.referee.mechanisms.PopulationMechanismFactory;

import java.util.Map;

import static java.lang.String.format;
import static org.javafunk.funk.Literals.iterableFrom;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PopulationEngine<T> {
    Class<T> klass;
    PopulationMechanismFactory mechanismFactory;

    public PopulationResult<T> process(Map<String, Object> definition) {
        ProblemReport problemReport = mechanismFactory.validateFor(klass, definition, ProblemReport.empty());

        if (problemReport.hasProblems()) {
            return new PopulationResult<>(null, problemReport);
        } else {
            T instance = mechanismFactory.populateFor(klass, definition);

            return new PopulationResult<>(instance, ProblemReport.empty());
        }
    }
}
