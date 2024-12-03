package models.expressions;

import exceptions.TypeException;
import models.adt.CloneableString;
import models.adt.IDict;
import models.adt.IHeap;
import models.types.IType;
import models.values.IValue;

public class ValueExp implements IExp {
    IValue value;

    public ValueExp(IValue value) {
        this.value = value;
    }

    public IValue eval(IDict<CloneableString, IValue> symTable, IHeap heap) {
        return value;
    }

    public IExp deepCopy() {
        return new ValueExp(value.deepCopy());
    }

    public String toString() {
        return value.toString();
    }

    @Override
    public IType typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        return value.getType();
    }
}