package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.types.IType;

public class CompStmt implements IStmt {
    IStmt lhs;
    IStmt rhs;

    @Override
    public String toString() {
        return "(" + lhs.toString() + "; " + rhs.toString() + ")";
    }

    public CompStmt(IStmt lhs, IStmt rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public CompStmt deepCopy() {
        return new CompStmt(lhs.deepCopy(), rhs.deepCopy());
    }

    public PrgState execute(PrgState state) throws ExecutionException {
        if (lhs == null || rhs == null) {
            throw new ExecutionException("Invalid CompStmt");
        }
        state.getExeStack().push(rhs);
        state.getExeStack().push(lhs);
        return null;
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        return rhs.typecheck(lhs.typecheck(typeEnvironment));
    }
}
