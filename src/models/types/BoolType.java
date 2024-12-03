package models.types;

import models.values.BoolValue;
import models.values.IValue;

public class BoolType implements IType {
    @Override
    public boolean equals(Object obj) {
        return obj instanceof BoolType;
    }

    @Override
    public String toString() {
        return "bool";
    }

    public IValue defaultValue() {
        return new BoolValue(false);
    }

    @Override
    public IType deepCopy() {
        return new BoolType();
    }
}