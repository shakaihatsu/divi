package divi.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;

public class TypeInfo {
    private String name = "";
    private TypeInfo superClass;
    private IJavaElement javaElement;
    private final Set<TypeInfo> immediateImplementedInterfaces = new HashSet<>();
    private final Map<String, TypeInfo> injectedFieldTypeInfo = new HashMap<String, TypeInfo>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeInfo getSuperClass() {
        return superClass;
    }

    public void setSuperClass(TypeInfo superClass) {
        this.superClass = superClass;
    }

    public IJavaElement getJavaElement() {
        return javaElement;
    }

    public void setJavaElement(IJavaElement javaElement) {
        this.javaElement = javaElement;
    }

    public Set<TypeInfo> getImmediateImplementedInterfaces() {
        return immediateImplementedInterfaces;
    }

    public Map<String, TypeInfo> getInjectedFieldTypeInfo() {
        return injectedFieldTypeInfo;
    }

    @Override
    public String toString() {
        return "TypeInfo [name=" + name + "]";
    }

}
