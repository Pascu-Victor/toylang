package models.expressions;

import models.adt.IDict;
import models.values.IValue;

public class ValueExp implements IExp {
    IValue value;

    public ValueExp(IValue value) {
        this.value = value;
    }

    public IValue eval(IDict<String, IValue> symTable) {
        return value;
    }

    public IExp deepCopy() {
        return new ValueExp(value.deepCopy());
    }

    public String toString() {
        return value.toString();
    }
}