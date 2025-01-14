package com.wmy.models.statements;

import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.types.IType;

public class NopStmt implements IStmt {
    public PrgState execute(PrgState state) {
        return null;
    }

    public String toString() {
        return "nop";
    }

    public NopStmt deepCopy() {
        return new NopStmt();
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        return typeEnvironment;
    }
}