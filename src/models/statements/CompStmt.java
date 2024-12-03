package models.statements;

import exceptions.ExecutionException;
import models.PrgState;

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
}
