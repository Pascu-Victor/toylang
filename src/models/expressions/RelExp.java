package models.expressions;

import exceptions.ExecutionException;
import models.adt.IDict;
import models.types.IntType;
import models.values.BoolValue;
import models.values.IValue;
import models.values.IntValue;

public class RelExp implements IExp {
    IExp lhs;
    IExp rhs;
    OpEnum op;

    public RelExp(IExp lhs, IExp rhs, OpEnum op) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
    }

    public IValue eval(IDict<String, IValue> symTable) throws ExecutionException {
        IValue lhsVal = lhs.eval(symTable);
        if(!lhsVal.getType().equals(new IntType())) {
            throw new ExecutionException("Invalid type for lhs, got: " + lhsVal.getType().toString());
        }
        IValue rhsVal = rhs.eval(symTable);
        if(!rhsVal.getType().equals((new IntType()))) {
            throw new ExecutionException("Invalid type for rhs, got: " + rhsVal.getType().toString());
        }

        IntValue i1 = (IntValue) lhsVal;
        int n1 = i1.getVal();
        IntValue i2 = (IntValue) rhsVal;
        int n2 = i2.getVal();

        switch (op) {
            case LESS:
                return new BoolValue(n1<n2);
            case LESSEQ:
                return new BoolValue(n1<=n2);
            case EQUAL:
                return new BoolValue(n1==n2);
            case NOTEQ:
                return new BoolValue(n1!=n2);
            case GREATER:
                return new BoolValue(n1>n2);
            case GREATEREQ:
                return new BoolValue(n1>=n2);
            default:
                throw new ExecutionException("Invalid operator, got" + op.toString());
        }
    }

    public IExp deepCopy() {
        return new ArithExp(lhs.deepCopy(), rhs.deepCopy(), op);
    }

    @Override
    public String toString() {
        return lhs.toString() + " " + op.toString() + " " + rhs.toString();
    }

}
