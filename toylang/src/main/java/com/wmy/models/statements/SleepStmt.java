package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.types.IType;

public class SleepStmt implements IStmt {
    private int time;

    public SleepStmt(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "sleep(" + time + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        if (time > 0) {
            state.getExeStack().push(new SleepStmt(time - 1));
        }
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new SleepStmt(time);
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        return typeEnvironment;
    }
}
