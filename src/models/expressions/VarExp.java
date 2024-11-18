package models.expressions;

import exceptions.ExecutionException;
import models.adt.IDict;
import models.adt.IHeap;
import models.values.IValue;

public class VarExp implements IExp {
    String id;

    public VarExp(String id) {
        this.id = id;
    }

    public IValue eval(IDict<String, IValue> symTable, IHeap heap) throws ExecutionException {
        return symTable.get(id);
    }

    public IExp deepCopy() {
        return new VarExp(id);
    }

    public String toString() {
        return id;
    }

}