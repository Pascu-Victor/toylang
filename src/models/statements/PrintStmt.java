package models.statements;

import exceptions.ExecutionException;
import exceptions.TypeException;
import models.PrgState;
import models.adt.CloneableString;
import models.adt.IDict;
import models.adt.IList;
import models.expressions.IExp;
import models.types.IType;
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
        return null;
    }

    public PrintStmt(IExp exp) {
        this.exp = exp;
    }

    @Override
    public IStmt deepCopy() {
        return new PrintStmt(exp.deepCopy());
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        exp.typecheck(typeEnvironment);
        return typeEnvironment;
    }
}
