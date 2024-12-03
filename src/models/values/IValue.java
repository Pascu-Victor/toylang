package models.values;

import models.adt.ICloneable;
import models.types.IType;

public interface IValue extends ICloneable {
    boolean equals(Object obj);
    IType getType();
    String toString();
    IValue deepCopy();
}