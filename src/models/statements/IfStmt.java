package models.statements;

import exceptions.ExecutionException;
import models.PrgState;
import models.expressions.IExp;
import models.values.BoolValue;

public class IfStmt implements IStmt {
    private IStmt thenS;
    private IStmt elseS;
    private IExp exp;

    public IfStmt(IExp exp, IStmt thenS, IStmt elseS) {
        this.exp = exp;
        this.thenS = thenS;
        this.elseS = elseS;
    }

    public IExp getExp() {
        return exp;
    }

    public String toString() {
        return "if (" + exp.toString() + ") then (" + thenS.toString() + ") else (" + elseS.toString() + ")";
    }

    public PrgState execute(PrgState state) throws ExecutionException {
        var r = exp.eval(state.getSymTable());
        if (r instanceof BoolValue) {
            if (((BoolValue)r).getVal()) {
                state.getExeStack().push(thenS);
            } else {
                state.getExeStack().push(elseS);
            }
            return state;
        }
        if(r == null)
            throw new ExecutionException("Invalid IfStmt: Expression evaluation returned null");
        throw new ExecutionException("Invalid IfStmt: Cannot coerce expression (Type: "+ r.getType().toString() +") to BoolValue");
    }

    public IfStmt deepCopy() {
        return new IfStmt(exp.deepCopy(), thenS.deepCopy(), elseS.deepCopy());
    }
    
}
