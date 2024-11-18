package models.expressions;

import exceptions.ExecutionException;
import models.adt.IDict;
import models.adt.IHeap;
import models.values.IValue;

public interface IExp {
    IValue eval(IDict<String, IValue> symTable, IHeap heap) throws ExecutionException;
    IExp deepCopy();
}