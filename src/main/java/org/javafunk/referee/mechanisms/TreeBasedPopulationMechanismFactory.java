package org.javafunk.referee.mechanisms;

import org.javafunk.funk.datastructures.tuples.Pair;
import org.javafunk.referee.ProblemReport;
import org.javafunk.referee.tree.Tree;
import org.javafunk.referee.tree.factories.fromclass.ElementMetadata;

import java.util.Map;

import static org.javafunk.funk.Objects.toStringValue;
import static org.javafunk.referee.tree.factories.fromclass.ClassToTree.fromClassToTree;
import static org.javafunk.referee.tree.factories.frommap.MapToTree.fromMapToTree;

public class TreeBasedPopulationMechanismFactory implements PopulationMechanismFactory {
    @Override
    public <C> ProblemReport validateFor(
            Class<C> targetType,
            Map<Object, Object> definition,
            ProblemReport problemReport) {
        return ProblemReport.empty();
    }

    @Override
    public <C> C populateFor(
            Class<C> targetType,
            Map<Object, Object> definition) {
        Tree<String, ElementMetadata> classDefinition = fromClassToTree().call(targetType);
        Tree<String, Object> valuesDefinition = fromMapToTree().call(definition)
                .mapLabels(toStringValue());

        Tree<String, Pair<ElementMetadata, Object>> classValues = classDefinition.zip(valuesDefinition);

        return null;
    }

    @Override
    public <C> PopulationMechanism<C> mechanismFor(Class<C> targetType) {
        return null;
    }
}
