package com.wmy.models.values;

import com.wmy.models.types.IType;
import com.wmy.models.types.IntType;

public class IntValue implements IValue {
    int val;

    public IntValue(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public IType getType() {
        return new IntType();
    }

    public IValue deepCopy() {
        return new IntValue(val);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntValue) {
            return ((IntValue) obj).getVal() == val;
        }
        return false;
    }

    @Override
    public String toString() {
        return Integer.toString(val);
    }
}
