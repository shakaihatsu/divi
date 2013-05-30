package divi.engine;

import java.util.Map;
import java.util.Set;

import divi.model.TypeInfo;
import divi.model.node.ClassNode;
import divi.model.node.DependencyNode;
import divi.model.node.Node;
import divi.model.node.ProjectNode;

public class TreeBuilderImpl implements TreeBuilder {

    @Override
    public Node buildTree(Map<String, Set<TypeInfo>> projectNameToClassTypeInfoSet, Map<String, Set<TypeInfo>> interfaceNameToImplementationSet) {
        Node rootNode = new Node();

        for (String projectName : projectNameToClassTypeInfoSet.keySet()) {
            ProjectNode projectNode = new ProjectNode();
            projectNode.setParent(rootNode);
            rootNode.getChildren().add(projectNode);
            projectNode.setName(projectName);

            for (TypeInfo classTypeInfo : projectNameToClassTypeInfoSet.get(projectName)) {
                ClassNode classNode = new ClassNode();

                Map<String, TypeInfo> injectedFieldTypeInfo = classTypeInfo.getInjectedFieldTypeInfo();

                if (!injectedFieldTypeInfo.isEmpty()) {
                    projectNode.getChildren().add(classNode);
                    classNode.setParent(rootNode);
                    classNode.setName(classTypeInfo.getName() + " (" + injectedFieldTypeInfo.size() + ")");
                    classNode.setTypeInfo(classTypeInfo);

                    for (Map.Entry<String, TypeInfo> injectedField : injectedFieldTypeInfo.entrySet()) {
                        DependencyNode dependencyNode = new DependencyNode();
                        classNode.getChildren().add(dependencyNode);
                        dependencyNode.setParent(classNode);
                        dependencyNode.setName(injectedField.getValue().getName());
                        dependencyNode.setVariableName(injectedField.getKey());
                        dependencyNode.setTypeInfo(injectedField.getValue());

                        if (interfaceNameToImplementationSet.containsKey(dependencyNode.getName())) {
                            for (TypeInfo implementationTypeInfo : interfaceNameToImplementationSet.get(dependencyNode.getName())) {
                                ClassNode implementationNode = new ClassNode();
                                implementationNode.setParent(dependencyNode);
                                dependencyNode.getChildren().add(implementationNode);
                                implementationNode.setName(implementationTypeInfo.getName());
                                implementationNode.setTypeInfo(implementationTypeInfo);
                            }
                        }
                    }
                }
            }
        }

        return rootNode;
    }

}
