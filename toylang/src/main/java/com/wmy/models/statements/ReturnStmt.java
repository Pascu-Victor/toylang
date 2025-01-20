package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.types.IType;

public class ReturnStmt implements IStmt {
    public ReturnStmt() {
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        state.popSymTable();
        return null;
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "return";
    }

    @Override
    public IStmt deepCopy() {
        return new ReturnStmt();
    }
}
