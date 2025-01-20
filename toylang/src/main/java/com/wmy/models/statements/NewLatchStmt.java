package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.expressions.IExp;
import com.wmy.models.types.IType;
import com.wmy.models.types.IntType;
import com.wmy.models.values.IntValue;

public class NewLatchStmt implements IStmt {
    CloneableString varName;
    IExp exp;

    public NewLatchStmt(CloneableString varName, IExp exp) {
        this.varName = varName;
        this.exp = exp;
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        if (!typeEnvironment.containsKey(varName)) {
            throw new TypeException("Variable " + varName + " not declared");
        }

        var type = typeEnvironment.get(varName);
        if (!type.equals(new IntType())) {
            throw new TypeException("Latch variable " + varName + " is not of type int");
        }

        if (!exp.typecheck(typeEnvironment).equals(new IntType())) {
            throw new TypeException("Expression " + exp + " is not of type int");
        }

        return typeEnvironment;
    }

    @Override
    public IStmt deepCopy() {
        return new NewLatchStmt(varName.deepCopy(), exp.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        var expRes = exp.eval(state.getSymTable(), state.getHeap());
        if (!expRes.getType().equals(new IntType())) {
            throw new ExecutionException("Expression " + exp + " is not of type int");
        }
        var expVal = ((IntValue) expRes).getVal();

        var expKey = state.getLatchTable().add(expVal);
        state.getSymTable().set(varName, new IntValue(expKey));

        return null;
    }
}
