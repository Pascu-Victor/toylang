package com.wmy.models.types;

import com.wmy.models.values.IValue;
import com.wmy.models.values.IntValue;

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

    @Override
    public IType deepCopy() {
        return new IntType();
    }
}
