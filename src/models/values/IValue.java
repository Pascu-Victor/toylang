package models.values;

import models.types.IType;

public interface IValue {
    boolean equals(Object obj);
    IType getType();
    String toString();
    IValue deepCopy();
}