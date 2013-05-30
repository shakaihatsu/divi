package divi.model.node;

import divi.model.TypeInfo;

public abstract class TypeNode extends ImageNode {
    private TypeInfo typeInfo;

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }

    public void setTypeInfo(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;
    }

}
