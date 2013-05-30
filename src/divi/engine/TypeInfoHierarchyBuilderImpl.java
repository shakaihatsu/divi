package divi.engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import divi.data.DiviConfiguration;
import divi.model.TypeInfo;

public class TypeInfoHierarchyBuilderImpl extends ASTVisitor implements TypeInfoHierarchyBuilder {
    private static final String UNKNOWN_TYPE_NAME = "UNKNOWN";
    private static final String UNKNOWN_VARIABLE_NAME = "UNKNOWN";

    private final String projectName;
    private final Map<String, Set<TypeInfo>> projectNameToClassTypeInfoSet;
    private final Map<String, Set<TypeInfo>> interfaceNameToImplementationSet;

    private final Map<String, TypeInfo> typeNameToTypeInfo;
    private final TypeInfo unknownTypeInfo;
    private final Set<String> injectAnnotationSet = DiviConfiguration.getInjectAnnotationSet();

    public TypeInfoHierarchyBuilderImpl(String projectName, Map<String, Set<TypeInfo>> projectNameToClassTypeInfoSet,
            Map<String, Set<TypeInfo>> interfaceNameToImplementationSet) {

        this.projectName = projectName;
        this.projectNameToClassTypeInfoSet = projectNameToClassTypeInfoSet;
        this.interfaceNameToImplementationSet = interfaceNameToImplementationSet;

        unknownTypeInfo = new TypeInfo();
        unknownTypeInfo.setName(UNKNOWN_TYPE_NAME);

        typeNameToTypeInfo = new HashMap<>();
        typeNameToTypeInfo.put(UNKNOWN_TYPE_NAME, unknownTypeInfo);
    }

    @Override
    public boolean visit(TypeDeclaration typeDeclaration) {
        ITypeBinding typeBinding = typeDeclaration.resolveBinding();
        TypeInfo typeInfo = fetchTypeInfo(typeBinding);

        registerClassTypeInfo(typeInfo);

        ITypeBinding[] interfaceTypeBindings = typeBinding.getInterfaces();
        registerTypeInfoToInterfaceNames(typeInfo, interfaceTypeBindings);

        return super.visit(typeDeclaration);
    }

    @Override
    public boolean visit(FieldDeclaration fieldDeclaration) {
        TypeInfo classTypeInfo = getClassTypeInfo(fieldDeclaration);
        registerClassTypeInfo(classTypeInfo);

        Type type = fieldDeclaration.getType();
        ITypeBinding typeBinding = type.resolveBinding();
        TypeInfo typeInfo = fetchTypeInfo(typeBinding);

        String variableName = UNKNOWN_VARIABLE_NAME;
        List<?> fragments = fieldDeclaration.fragments();
        for (Object fragment : fragments) {
            if (fragment instanceof org.eclipse.jdt.core.dom.VariableDeclarationFragment) {
                org.eclipse.jdt.core.dom.VariableDeclarationFragment variableDeclarationFragment = (org.eclipse.jdt.core.dom.VariableDeclarationFragment) fragment;
                // TODO Any better way of getting the actual variable name?
                variableName = variableDeclarationFragment.getName().toString();
                // TODO open variable declaration instead of class declaration on doubleClick
                // typeInfo.setJavaElement(variableDeclarationFragment.resolveBinding().getJavaElement());
            }
        }

        Object modifiers = fieldDeclaration.getStructuralProperty(FieldDeclaration.MODIFIERS2_PROPERTY);
        if (modifiers instanceof Iterable<?>) {
            for (Object modifier : (Iterable<?>) modifiers) {
                if (modifier instanceof org.eclipse.jdt.core.dom.Annotation) {
                    final org.eclipse.jdt.core.dom.Annotation annotation = (org.eclipse.jdt.core.dom.Annotation) modifier;

                    final IAnnotationBinding annotationBinding = annotation.resolveAnnotationBinding();

                    String annotationBindingName = annotationBinding.getName();

                    if (injectAnnotationSet.contains(annotationBindingName)) {
                        classTypeInfo.getInjectedFieldTypeInfo().put(variableName, typeInfo);
                    }
                }
            }
        } else {
            // Shouldn't happen
        }

        return super.visit(fieldDeclaration);
    }

    private void registerClassTypeInfo(TypeInfo classTypeInfo) {
        Set<TypeInfo> classTypeInfoSet = projectNameToClassTypeInfoSet.get(projectName);
        if (classTypeInfoSet == null) {
            classTypeInfoSet = new HashSet<>();
            projectNameToClassTypeInfoSet.put(projectName, classTypeInfoSet);
        }
        classTypeInfoSet.add(classTypeInfo);
    }

    private void registerTypeInfoToInterfaceNames(TypeInfo typeInfo, ITypeBinding[] interfaceTypeBindings) {
        for (ITypeBinding interfaceTypeBinding : interfaceTypeBindings) {
            String interfaceQualifiedName = interfaceTypeBinding.getQualifiedName();

            Set<TypeInfo> implementationSet = interfaceNameToImplementationSet.get(interfaceQualifiedName);

            if (implementationSet == null) {
                implementationSet = new HashSet<>();
                interfaceNameToImplementationSet.put(interfaceQualifiedName, implementationSet);
            }

            implementationSet.add(typeInfo);

            registerTypeInfoToInterfaceNames(typeInfo, interfaceTypeBinding.getInterfaces());
        }
    }

    private TypeInfo getClassTypeInfo(FieldDeclaration fieldDeclaration) {
        TypeInfo classTypeInfo;

        switch (fieldDeclaration.getParent().getNodeType()) {
            case ASTNode.TYPE_DECLARATION:
                TypeDeclaration typeDeclaration = (TypeDeclaration) fieldDeclaration.getParent();
                classTypeInfo = fetchTypeInfo(typeDeclaration.resolveBinding());
                break;
            case ASTNode.ANONYMOUS_CLASS_DECLARATION:
                AnonymousClassDeclaration anonymousClassDeclaration = (AnonymousClassDeclaration) fieldDeclaration.getParent();
                // Give a better anonymous name?
                classTypeInfo = fetchTypeInfo(anonymousClassDeclaration.resolveBinding(), "Anonymous$" + anonymousClassDeclaration.hashCode());
                break;
            default:
                classTypeInfo = unknownTypeInfo;
                break;
        }

        return classTypeInfo;
    }

    private TypeInfo fetchTypeInfo(ITypeBinding typeBinding) {
        if (typeBinding == null) {
            return unknownTypeInfo;
        }

        return fetchTypeInfo(typeBinding, typeBinding.getQualifiedName());
    }

    private TypeInfo fetchTypeInfo(ITypeBinding typeBinding, String name) {
        TypeInfo typeInfo;

        if (typeBinding == null) {
            return unknownTypeInfo;
        }

        if (typeNameToTypeInfo.containsKey(name)) {
            typeInfo = typeNameToTypeInfo.get(name);
        } else {
            IJavaElement javaElement = typeBinding.getJavaElement();

            typeInfo = new TypeInfo();
            typeInfo.setName(name);
            typeInfo.setSuperClass(fetchTypeInfo(typeBinding.getSuperclass()));
            typeInfo.setJavaElement(javaElement);

            for (ITypeBinding interFace : typeBinding.getInterfaces()) {
                typeInfo.getImmediateImplementedInterfaces().add(fetchTypeInfo(interFace));
            }

            typeNameToTypeInfo.put(name, typeInfo);
        }

        return typeInfo;
    }

    @Override
    public Map<String, Set<TypeInfo>> getProjectNameToClassTypeInfoSet() {
        return projectNameToClassTypeInfoSet;
    }

    @Override
    public Map<String, Set<TypeInfo>> getInterfaceNameToImplementationSet() {
        return interfaceNameToImplementationSet;
    }

}
