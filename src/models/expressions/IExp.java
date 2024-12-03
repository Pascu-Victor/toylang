package models.expressions;

import exceptions.ExecutionException;
import exceptions.TypeException;
import models.adt.CloneableString;
import models.adt.IDict;
import models.adt.IHeap;
import models.types.IType;
import models.values.IValue;

public interface IExp {
    IValue eval(IDict<CloneableString, IValue> symTable, IHeap heap) throws ExecutionException;
    IType typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException;
    IExp deepCopy();
}