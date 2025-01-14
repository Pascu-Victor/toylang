package com.wmy.models.expressions;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.adt.IHeap;
import com.wmy.models.types.IType;
import com.wmy.models.values.IValue;

public interface IExp {
    IValue eval(IDict<CloneableString, IValue> symTable, IHeap heap) throws ExecutionException;
    IType typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException;
    IExp deepCopy();
}