package com.wmy.models.expressions;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.adt.IHeap;
import com.wmy.models.types.BoolType;
import com.wmy.models.types.IType;
import com.wmy.models.values.BoolValue;
import com.wmy.models.values.IValue;

public class CondExp implements IExp {
    private IExp cond;
    private IExp thenExp;
    private IExp elseExp;

    public CondExp(IExp cond, IExp thenExp, IExp elseExp) {
        this.cond = cond;
        this.thenExp = thenExp;
        this.elseExp = elseExp;
    }

    @Override
    public IValue eval(IDict<CloneableString, IValue> symTable, IHeap heap) throws ExecutionException {
        IValue condValue = cond.eval(symTable, heap);
        if (!condValue.getType().equals(new BoolType())) {
            throw new ExecutionException("The condition of the conditional expression is not a boolean");
        }
        if (((BoolValue) condValue).getVal()) {
            return thenExp.eval(symTable, heap);
        } else {
            return elseExp.eval(symTable, heap);
        }
    }

    @Override
    public IType typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        IType condType = cond.typecheck(typeEnvironment);
        if (!condType.equals(new BoolType())) {
            throw new TypeException("The condition of the conditional expression is not a boolean");
        }
        IType thenType = thenExp.typecheck(typeEnvironment);
        IType elseType = elseExp.typecheck(typeEnvironment);
        if (!thenType.equals(elseType)) {
            throw new TypeException("The types of the branches of the conditional expression do not match");
        }
        return thenType;
    }

    @Override
    public IExp deepCopy() {
        return new CondExp(cond.deepCopy(), thenExp.deepCopy(), elseExp.deepCopy());
    }

    @Override
    public String toString() {
        return cond + "?" + thenExp + ":" + elseExp;
    }
}
