package models.expressions;

import exceptions.ExecutionException;
import models.adt.IDict;
import models.values.BoolValue;
import models.values.IValue;

public class LogicExp implements IExp {
    IExp exp1;
    IExp exp2;
    String op;

    public LogicExp(IExp exp1, IExp exp2, String op) {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.op = op;
    }

    public IValue eval(IDict<String, IValue> symTable) throws ExecutionException {
        IValue v1 = exp1.eval(symTable);
        IValue v2 = exp2.eval(symTable);

        if (v1.getType() != v2.getType()) {
            throw new ExecutionException("Types do not match");
        }

        switch (op) {
            case "&&":
                return new BoolValue(((BoolValue) v1).getVal() && ((BoolValue) v2).getVal());
            case "||":
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
