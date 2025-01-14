package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.expressions.IExp;
import com.wmy.models.types.BoolType;
import com.wmy.models.types.IType;
import com.wmy.models.values.BoolValue;
import com.wmy.models.values.IValue;

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

        return null;
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        IType condType = condExp.typecheck(typeEnvironment);
        if (!condType.equals(new BoolType())) {
            throw new TypeException("While condition must be a boolean");
        }
        stmts.typecheck(typeEnvironment);
        return typeEnvironment;
    }

}