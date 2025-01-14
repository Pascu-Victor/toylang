package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.adt.IList;
import com.wmy.models.expressions.IExp;
import com.wmy.models.types.IType;
import com.wmy.models.values.IValue;

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
