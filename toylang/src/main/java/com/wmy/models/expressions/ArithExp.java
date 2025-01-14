package com.wmy.models.expressions;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.adt.IHeap;
import com.wmy.models.types.IType;
import com.wmy.models.types.IntType;
import com.wmy.models.values.IValue;
import com.wmy.models.values.IntValue;

public class ArithExp implements IExp {
    IExp lhs;
    IExp rhs;
    OpEnum op;

    public ArithExp(IExp lhs, IExp rhs, OpEnum op) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.op = op;
    }

    public IValue eval(IDict<CloneableString, IValue> symTable, IHeap heap) throws ExecutionException {
        IValue lhsVal = lhs.eval(symTable, heap);
        if(!lhsVal.getType().equals(new IntType())) {
            throw new ExecutionException("Invalid type for lhs, got: " + lhsVal.getType().toString());
        }
        IValue rhsVal = rhs.eval(symTable, heap);
        if(!rhsVal.getType().equals((new IntType()))) {
            throw new ExecutionException("Invalid type for rhs, got: " + rhsVal.getType().toString());
        }

        IntValue i1 = (IntValue) lhsVal;
        int n1 = i1.getVal();
        IntValue i2 = (IntValue) rhsVal;
        int n2 = i2.getVal();

        switch (op) {
            case PLUS:
                return new IntValue(n1+n2);
            case MINUS:
                return new IntValue(n1-n2);
            case STAR:
                return new IntValue(n1*n2);
            case DIVIDE:
                if(n2 == 0) {
                    throw new ExecutionException("Division by zero");
                }
                return new IntValue(n1/n2);
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

    @Override
    public IType typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        IType tlhs = lhs.typecheck(typeEnvironment);
        IType trhs = rhs.typecheck(typeEnvironment);
        if(!tlhs.equals(new IntType())) {
            throw new TypeException("Rhs operand is not of type IntType, got: " + trhs);
        }
        if (!trhs.equals(new IntType())) {
            throw new TypeException("Rhs operand is not of type IntType, got: " + trhs);
        }
        return new IntType();
    }
}
