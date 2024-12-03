package models.statements;

import exceptions.ExecutionException;
import exceptions.TypeException;
import models.PrgState;
import models.adt.AStack;
import models.adt.CloneableString;
import models.adt.IDict;
import models.types.IType;

public class ForkStmt implements IStmt {
    IStmt subprogram;

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        var stk = new AStack<IStmt>();
        return new PrgState(stk, state.getSymTable().deepCopy(), state.getOut(), state.getFileTable(), state.getHeap(), subprogram);
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
        return "fork("+subprogram+")";
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        subprogram.typecheck(typeEnvironment.deepCopy());
        return typeEnvironment;
    }
}
