package divi.engine;

import java.util.Map;
import java.util.Set;

import divi.model.TypeInfo;

public interface TypeInfoHierarchyBuilder {
    Map<String, Set<TypeInfo>> getProjectNameToClassTypeInfoSet();

    Map<String, Set<TypeInfo>> getInterfaceNameToImplementationSet();
}
