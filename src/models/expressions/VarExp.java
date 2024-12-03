package models.expressions;

import exceptions.ExecutionException;
import models.adt.CloneableString;
import models.adt.IDict;
import models.adt.IHeap;
import models.values.IValue;

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

}