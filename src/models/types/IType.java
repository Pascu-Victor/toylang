package models.types;

import models.adt.ICloneable;
import models.values.IValue;

public interface IType extends ICloneable {
    boolean equals(Object obj);
    IValue defaultValue();
    IType deepCopy();
}
