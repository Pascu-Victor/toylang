package com.wmy.models.values;

import com.wmy.models.adt.ICloneable;
import com.wmy.models.types.IType;

public interface IValue extends ICloneable {
    boolean equals(Object obj);
    IType getType();
    String toString();
    IValue deepCopy();
}