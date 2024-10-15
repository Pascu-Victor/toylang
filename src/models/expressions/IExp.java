package models.expressions;

import exceptions.ExecutionException;
import models.adt.IDict;
import models.values.IValue;

public interface IExp {
    IValue eval(IDict<String, IValue> symTable) throws ExecutionException;
    IExp deepCopy();
}