package models.statements;

import exceptions.ExecutionException;
import models.PrgState;
import models.adt.CloneableString;
import models.expressions.IExp;
import models.types.RefType;
import models.values.IValue;
import models.values.RefValue;

public class NewStmt implements IStmt {
    private CloneableString varName;
    private IExp exp;

    public NewStmt(CloneableString varName, IExp exp) {
        this.varName = varName;
        this.exp = exp;
    }

    public NewStmt(String varName, IExp exp) {
        this.varName = new CloneableString(varName);
        this.exp = exp;
    }

    public CloneableString getVarName() {
        return varName;
    }

    public IExp getExp() {
        return exp;
    }

    @Override
    public String toString() {
        return "new(" + varName + "," + exp + ")";
    }

    public IStmt deepCopy() {
        return new NewStmt(varName.deepCopy(), exp.deepCopy());
    }

    public PrgState execute(PrgState state) throws ExecutionException {
        IValue val = exp.eval(state.getSymTable(), state.getHeap());
        IValue ref = state.getSymTable().get(varName);
        if (!ref.getType().equals(new RefType(val.getType()))) {
            throw new ExecutionException("Variable " + varName + " is not a ref to " + val.getType());
        }

        int addr = state.getHeap().add(val);
        var type = ((RefType)ref.getType()).getInner();
        state.getSymTable().set(varName, new RefValue(addr, type));
        
        return null;
    }
}
