package com.wmy.models.values;

import com.wmy.models.types.BoolType;
import com.wmy.models.types.IType;

public class BoolValue implements IValue {
    private boolean val;

    public BoolValue(boolean val) {
        this.val = val;
    }

    public boolean getVal() {
        return val;
    }

    public IType getType() {
        return new BoolType();
    }

    public IValue deepCopy() {
        return new BoolValue(val);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BoolValue) {
            return ((BoolValue) obj).getVal() == val;
        }
        return false;
    }

    @Override
    public String toString() {
        return Boolean.toString(val);
    }
}
