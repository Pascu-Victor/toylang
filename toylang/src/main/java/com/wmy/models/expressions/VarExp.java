package com.wmy.models.expressions;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.adt.IHeap;
import com.wmy.models.types.IType;
import com.wmy.models.values.IValue;

public class VarExp implements IExp {
    CloneableString id;

    public VarExp(CloneableString id) {
        this.id = id;
    }

    public VarExp(String id) {
        this.id = new CloneableString(id);
    }

    public IValue eval(IDict<CloneableString, IValue> symTable, IHeap heap) throws ExecutionException {
        return symTable.get(id);
    }

    public IExp deepCopy() {
        return new VarExp(id);
    }

    public String toString() {
        return id.toString();
    }

    @Override
    public IType typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        return typeEnvironment.get(id);
    }
}