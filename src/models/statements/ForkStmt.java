package models.statements;

import exceptions.ExecutionException;
import models.PrgState;
import models.adt.AStack;

public class ForkStmt implements IStmt {
    IStmt subprogram;

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        var stk = new AStack<IStmt>();
        stk.push(subprogram);
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
}
