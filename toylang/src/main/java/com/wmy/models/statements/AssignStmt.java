package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.expressions.IExp;
import com.wmy.models.types.IType;

public class AssignStmt implements IStmt {
    CloneableString id;
    IExp exp;

    public AssignStmt(CloneableString id, IExp exp) {
        this.id = id;
        this.exp = exp;
    }

    public AssignStmt(String id, IExp exp) {
        this.id = new CloneableString(id);
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "assign("+this.id + " = " + exp.toString()+")";
    }

    public IStmt deepCopy() {
        return new AssignStmt(this.id.deepCopy(), exp.deepCopy());
    }

    public PrgState execute(PrgState state) throws ExecutionException {
        state.getSymTable().set(this.id, this.exp.eval(state.getSymTable(), state.getHeap()));
        return null;
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        IType varType = typeEnvironment.get(id);
        IType expType = exp.typecheck(typeEnvironment);
        if (!varType.equals(expType)) {
            throw new TypeException("Assignment: rhs type (" + expType + ") is not the same as lhs type (" + varType + ")");
        }
        return typeEnvironment;
    }
}
