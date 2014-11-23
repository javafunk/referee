package org.javafunk.referee;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;
import org.javafunk.funk.Literals;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.referee.conversion.CoercionEngine;
import org.javafunk.referee.conversion.CoercionKey;
import org.javafunk.referee.conversion.FunctionBasedCoercionEngine;
import org.javafunk.referee.mechanisms.BuilderPopulationMechanismFactory;
import org.javafunk.referee.mechanisms.DirectFieldPopulationMechanismFactory;
import org.javafunk.referee.mechanisms.FirstApplicablePopulationMechanismFactory;
import org.javafunk.referee.mechanisms.PopulationMechanismFactory;

import java.util.Map;

import static org.javafunk.funk.Literals.mapBuilderFromEntries;

@Value
@AllArgsConstructor
public class PopulationEngineBuilder {
    @Wither(AccessLevel.PRIVATE) Map<CoercionKey, UnaryFunction<? extends Object, ? extends Object>> coercionFunctions;
    @Wither CoercionEngine coercionEngine;
    @Wither PopulationMechanismFactory populationMechanismFactory;

    public static PopulationEngineBuilder populationEngine() {
        return new PopulationEngineBuilder();
    }

    PopulationEngineBuilder() {
        this.coercionFunctions = Literals.map();
        this.coercionEngine = FunctionBasedCoercionEngine.withDefaultCoercions();
        this.populationMechanismFactory = new FirstApplicablePopulationMechanismFactory(
                new BuilderPopulationMechanismFactory(coercionEngine),
                new DirectFieldPopulationMechanismFactory(coercionEngine));
    }

    public PopulationEngineBuilder withDefaultCoercions() {
        FunctionBasedCoercionEngine coercionEngine = FunctionBasedCoercionEngine.withDefaultCoercions();
        return this
                .withCoercionEngine(coercionEngine)
                .withCoercionFunctions(coercionEngine.getCoercions());
    }

    public PopulationEngineBuilder withNoCoercions() {
        FunctionBasedCoercionEngine coercionEngine = FunctionBasedCoercionEngine.withNoCoercions();
        return this
                .withCoercionEngine(coercionEngine)
                .withCoercionFunctions(coercionEngine.getCoercions());
    }

    public PopulationEngineBuilder registerCoercion(
            Class<?> source,
            Class<?> target,
            UnaryFunction<? extends Object, ? extends Object> converter) {
        FunctionBasedCoercionEngine coercionEngine = FunctionBasedCoercionEngine
                .withCoercions(coercionFunctions)
                .registerCoercion(source, target, converter);
        return this
                .withCoercionEngine(coercionEngine)
                .withCoercionFunctions(coercionEngine.getCoercions());
    }

    public <T> PopulationEngine<T> forType(Class<T> target) {
        return new PopulationEngine<>(target, coercionEngine, populationMechanismFactory);
    }
}
