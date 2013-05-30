package divi.engine;

import java.util.Map;
import java.util.Set;

import divi.model.TypeInfo;
import divi.model.node.Node;

public interface TreeBuilder {
    public Node buildTree(Map<String, Set<TypeInfo>> projectNameToClassTypeInfoSet, Map<String, Set<TypeInfo>> interfaceNameToImplementationSet);
}
