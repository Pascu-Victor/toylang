package models.statements;

import exceptions.ExecutionException;
import models.PrgState;
import models.expressions.IExp;

public class AssignStmt implements IStmt {
    String id;
    IExp exp;

    public AssignStmt(String id, IExp exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "assign("+this.id + " = " + exp.toString()+")";
    }

    public IStmt deepCopy() {
        return new AssignStmt(this.id, exp.deepCopy());
    }

    public PrgState execute(PrgState state) throws ExecutionException {
        state.getSymTable().set(this.id, this.exp.eval(state.getSymTable()));
        return state;
    }
}
