package models.types;

import models.values.IValue;

public interface IType {
    boolean equals(Object obj);
    IValue defaultValue();
}
