package com.wmy.models.types;

import com.wmy.models.adt.ICloneable;
import com.wmy.models.values.IValue;

public interface IType extends ICloneable {
    boolean equals(Object obj);
    IValue defaultValue();
    IType deepCopy();
}
