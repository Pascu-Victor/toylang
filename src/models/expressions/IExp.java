package models.expressions;

import exceptions.ExecutionException;
import models.adt.CloneableString;
import models.adt.IDict;
import models.adt.IHeap;
import models.values.IValue;

public interface IExp {
    IValue eval(IDict<CloneableString, IValue> symTable, IHeap heap) throws ExecutionException;
    IExp deepCopy();
}