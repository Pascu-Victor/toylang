package models.statements;

import exceptions.ExecutionException;
import models.PrgState;
import models.adt.IList;
import models.expressions.IExp;
import models.values.IValue;

public class PrintStmt implements IStmt {
    private IExp exp;

    @Override
    public String toString() {
        return "print(" + exp.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        if (exp == null) {
            throw new ExecutionException("Invalid PrintStmt");
        }
        IList<IValue> out = state.getOut();
        out.add(exp.eval(state.getSymTable(), state.getHeap()));
        return state;
    }

    public PrintStmt(IExp exp) {
        this.exp = exp;
    }

    @Override
    public IStmt deepCopy() {
        return new PrintStmt(exp.deepCopy());
    }
}
