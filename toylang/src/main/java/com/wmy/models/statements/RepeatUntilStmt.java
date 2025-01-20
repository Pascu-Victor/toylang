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

public class RepeatUntilStmt implements IStmt {
    IExp untilExp;
    IStmt stmt;

    public RepeatUntilStmt(IExp untillExp, IStmt stmt) {
        this.untilExp = untillExp;
        this.stmt = stmt;
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        IType untillType = untilExp.typecheck(typeEnvironment);
        if (!untillType.equals(new BoolType())) {
            throw new TypeException("Repeat until condition must be a boolean");
        }
        stmt.typecheck(typeEnvironment.deepCopy());
        return typeEnvironment;
    }

    @Override
    public IStmt deepCopy() {
        return new RepeatUntilStmt(untilExp.deepCopy(), stmt.deepCopy());
    }

    @Override
    public String toString() {
        return "repeat { " + stmt + " } until (" + untilExp + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        var untillRes = untilExp.eval(state.getSymTable(), state.getHeap());
        if (!(untillRes.getType() instanceof BoolType)) {
            throw new ExecutionException(
                    "Expression did not resolve to BoolType, got " + untillRes + ", type" + untillRes.getType());
        }
        if (((BoolValue) untillRes).getVal()) {
            return null;
        }
        state.getExeStack().push(this);
        state.getExeStack().push(stmt);
        return null;
    }
}
