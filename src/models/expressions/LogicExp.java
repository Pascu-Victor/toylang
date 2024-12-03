package models.expressions;

import exceptions.ExecutionException;
import models.adt.CloneableString;
import models.adt.IDict;
import models.adt.IHeap;
import models.values.BoolValue;
import models.values.IValue;

public class LogicExp implements IExp {
    IExp exp1;
    IExp exp2;
    OpEnum op;

    public LogicExp(IExp exp1, IExp exp2, OpEnum op) {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.op = op;
    }

    public IValue eval(IDict<CloneableString, IValue> symTable, IHeap heap) throws ExecutionException {
        IValue v1 = exp1.eval(symTable, heap);
        IValue v2 = exp2.eval(symTable, heap);

        if (!v1.getType().equals(v2.getType())) {
            throw new ExecutionException("Types do not match");
        }

        switch (op) {
            case AND:
                return new BoolValue(((BoolValue) v1).getVal() && ((BoolValue) v2).getVal());
            case OR:
                return new BoolValue(((BoolValue) v1).getVal() || ((BoolValue) v2).getVal());
            default:
                throw new ExecutionException("Invalid operator");
        }
    }

    public IExp deepCopy() {
        return new LogicExp(exp1.deepCopy(), exp2.deepCopy(), op);
    }

    public String toString() {
        return exp1.toString() + " " + op + " " + exp2.toString();
    }
}
