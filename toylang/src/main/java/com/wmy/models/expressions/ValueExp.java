package com.wmy.models.expressions;

import com.wmy.exceptions.TypeException;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.adt.IHeap;
import com.wmy.models.types.IType;
import com.wmy.models.values.IValue;

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