package models.types;

import models.values.IValue;
import models.values.IntValue;

public class IntType implements IType {
    @Override
    public boolean equals(Object obj) {
        return obj instanceof IntType;
    }

    @Override
    public String toString() {
        return "int";
    }

    public IValue defaultValue() {
        return new IntValue(0);
    }
}
