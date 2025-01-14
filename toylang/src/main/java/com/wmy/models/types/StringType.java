package com.wmy.models.types;

import com.wmy.models.values.IValue;
import com.wmy.models.values.StringValue;

public class StringType implements IType {
    @Override
    public boolean equals(Object obj) {
        return obj instanceof StringType;
    }

    @Override
    public String toString() {
        return "string";
    }

    public IValue defaultValue() {
        return new StringValue("");
    }

    @Override
    public IType deepCopy() {
        return new StringType();
    }
}
