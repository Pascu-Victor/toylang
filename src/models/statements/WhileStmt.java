package models.statements;

import exceptions.ExecutionException;
import models.PrgState;
import models.expressions.IExp;
import models.types.BoolType;
import models.values.BoolValue;
import models.values.IValue;

public class WhileStmt implements IStmt {
    IExp condExp;
    IStmt stmts;

    public WhileStmt(IExp exp, IStmt stmts) {
        this.condExp = exp;
        this.stmts = stmts;
    }

    public IStmt deepCopy() {
        return new WhileStmt(condExp.deepCopy(), stmts.deepCopy());
    }

    @Override
    public String toString() {
        return "while("+condExp+"){ "+stmts+" }";
    }

    public PrgState execute(PrgState state) throws ExecutionException {
        IValue expRes = condExp.eval(state.getSymTable(), state.getHeap());

        if(!(expRes.getType() instanceof BoolType)) {
            throw new ExecutionException("Expression did not resolve to BoolType, got " + expRes + ", type" + expRes.getType());
        }
         
        if (((BoolValue)expRes).getVal()) {
            state.getExeStack().push(this);
            state.getExeStack().push(stmts);
        }

        return state;
    }

}