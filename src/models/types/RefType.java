package models.types;

import models.values.IValue;
import models.values.RefValue;

public class RefType implements IType {
    IType inner;
    public RefType(IType inner) {
        this.inner = inner;
    }

    public IType getInner() {
        return inner;
    }

    public boolean equals(Object other) {
        if(other instanceof RefType) {
            return inner.equals(((RefType)other).getInner());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Ref(" + inner +  ")";
    }

    public IValue defaultValue() {
        return new RefValue(0, inner);
    }

    @Override
    public IType deepCopy() {
        return new RefType(inner.deepCopy());
    }
}
