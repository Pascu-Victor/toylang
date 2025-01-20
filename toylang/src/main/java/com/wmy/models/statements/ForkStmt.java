package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.AStack;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.types.IType;

public class ForkStmt implements IStmt {
    IStmt subprogram;

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        var stk = new AStack<IStmt>();
        return new PrgState(stk, state.getSymTable().deepCopy(), state.getOut(), state.getFileTable(), state.getHeap(),
                state.getLatchTable(), subprogram);
    }

    public ForkStmt(IStmt subprogram) {
        this.subprogram = subprogram;
    }

    @Override
    public IStmt deepCopy() {
        return new ForkStmt(subprogram.deepCopy());
    }

    @Override
    public String toString() {
        return "fork(" + subprogram + ")";
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        subprogram.typecheck(typeEnvironment.deepCopy());
        return typeEnvironment;
    }
}
