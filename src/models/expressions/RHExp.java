package models.expressions;

import exceptions.ExecutionException;
import models.adt.CloneableString;
import models.adt.IDict;
import models.adt.IHeap;
import models.values.IValue;
import models.values.RefValue;

public class RHExp implements IExp {
    IExp ref;
    
    public RHExp(IExp ref) {
        this.ref = ref;
    }

    public IValue eval(IDict<CloneableString, IValue> symTable, IHeap heap) throws ExecutionException {
        IValue val = this.ref.eval(symTable, heap);
        if (val instanceof RefValue) {
            RefValue refVal = (RefValue) val;
            return heap.at(refVal.getAddr());
        } else {
            throw new ExecutionException("Expression is not a reference");
        }
    }

    public IExp deepCopy() {
        return new RHExp(ref.deepCopy());
    }

    public String toString() {
        return "rH(" + ref.toString() + ")";
    }

}
