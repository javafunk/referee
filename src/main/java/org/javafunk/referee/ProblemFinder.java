package org.javafunk.referee;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.javafunk.funk.Lazily;
import org.javafunk.funk.datastructures.tuples.Pair;
import org.javafunk.funk.monads.Option;
import org.javafunk.referee.support.EnrichedClass;
import org.javafunk.referee.support.Objects;
import org.javafunk.referee.tree.Node;
import org.javafunk.referee.tree.Tree;
import org.javafunk.referee.tree.factories.fromclass.ElementMetadata;
import org.javafunk.referee.tree.traversalhandlers.NoOpTraversalHandler;

import java.util.Map;

import static org.javafunk.funk.Literals.iterableBuilderFrom;
import static org.javafunk.funk.Literals.iterableOf;
import static org.javafunk.referee.ProblemFinder.ProblemFindingTraversalHandler.findingProblems;
import static org.javafunk.referee.tree.ZipMode.Loose;
import static org.javafunk.referee.tree.factories.fromclass.ClassToTree.fromClassToTree;
import static org.javafunk.referee.tree.factories.frommap.MapToTree.fromMapToTree;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProblemFinder {
    Tree<Object, Pair<Option<Object>, Option<ElementMetadata>>> tree;

    public ProblemFinder(Map<Object, Object> source, Class<?> target) {
        Tree<Object, Object> sourceTree = fromMapToTree().call(source);
        Tree<Object, ElementMetadata> targetTree = fromClassToTree().call(target)
                .mapLabels(Objects.toObjectFrom(String.class));

        this.tree = sourceTree.zip(Loose, targetTree);
    }

    public ProblemReport getReport() {
        return ProblemReport.of(tree.traverse(findingProblems()).getProblems());
    }

    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class ProblemFindingTraversalHandler
            extends NoOpTraversalHandler<Object, Pair<Option<Object>, Option<ElementMetadata>>> {
        @Getter @NonFinal Iterable<Problem> problems;
        @NonFinal Iterable<Object> currentPath;
        @NonFinal EnrichedClass<?> currentClass;
        @NonFinal Boolean hadProblem;

        public static ProblemFindingTraversalHandler findingProblems() {
            return new ProblemFindingTraversalHandler(
                    iterableOf(Problem.class),
                    iterableOf(Object.class),
                    null,
                    false);
        }

        @Override public void handleSelf(Node<Object, Pair<Option<Object>, Option<ElementMetadata>>> self) {
            currentClass = self.getValue().getSecond().get().getPossibleClass().get();
            currentPath = iterableBuilderFrom(currentPath)
                    .with(self.getLabel())
                    .build();
        }

        @Override public void handleChild(Integer index, Node<Object, Pair<Option<Object>, Option<ElementMetadata>>> child) {
            Object label = child.getLabel();
            Pair<Option<Object>, Option<ElementMetadata>> attributeDefinition = child.getValue();
            Option<Object> possibleAttributeValue = attributeDefinition.getFirst();
            Option<ElementMetadata> possibleElementMetadata = attributeDefinition.getSecond();

            if ((possibleAttributeValue.hasValue() || child.hasChildren())
                    && (possibleElementMetadata.hasNoValue() || possibleElementMetadata.get().hasNoClass())) {
                Iterable<Object> childPath = iterableBuilderFrom(currentPath).with(label).build();
                Iterable<String> childPathStrings = Lazily.map(childPath, org.javafunk.funk.Objects.toStringValue());
                String address = String.join(".", childPathStrings);

                hadProblem = true;
                problems = iterableBuilderFrom(problems)
                        .with(Problems.missingFieldProblem(address, currentClass.getUnderlyingClass()))
                        .build();
            }
        }

        @Override public boolean goDeeper(Node<Object, Pair<Option<Object>, Option<ElementMetadata>>> node) {
            return !hadProblem;
        }
    }
}
