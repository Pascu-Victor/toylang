package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.AList;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.expressions.IExp;
import com.wmy.models.types.IType;
import com.wmy.models.types.IntType;
import com.wmy.models.values.IntValue;

public class NewBarrierStmt implements IStmt {
    private CloneableString varName;
    private IExp exp;

    public NewBarrierStmt(CloneableString varName, IExp exp) {
        this.varName = varName;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "newBarrier(" + varName + ", " + exp + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        var expRes = this.exp.eval(state.getSymTable(), state.getHeap());
        var barrierTable = state.getBarrierTable();
        synchronized (barrierTable) {
            if (!state.getSymTable().containsKey(varName)) {
                throw new ExecutionException("Barrier variable " + varName + " not declared");
            }
            if (!state.getSymTable().get(varName).getType().equals(new IntType())) {
                throw new ExecutionException("Barrier variable " + varName + " not of type int");
            }
            if (!expRes.getType().equals(new IntType())) {
                throw new ExecutionException("Barrier expression " + exp + " not of type int");
            }
            var count = ((IntValue) expRes).getVal();
            var waitList = new AList<Integer>();
            var barrierId = barrierTable.add(count, waitList);
            state.getSymTable().set(varName, new IntValue(barrierId));
        }
        return null;
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        if (!typeEnvironment.containsKey(varName)) {
            throw new TypeException("Barrier variable " + varName + " not declared");
        }
        if (!typeEnvironment.get(varName).equals(new IntType())) {
            throw new TypeException("Barrier variable " + varName + " not of type int");
        }
        if (!exp.typecheck(typeEnvironment).equals(new IntType())) {
            throw new TypeException("Barrier expression " + exp + " not of type int");
        }
        return typeEnvironment;
    }

    @Override
    public IStmt deepCopy() {
        return new NewBarrierStmt(varName.deepCopy(), exp.deepCopy());
    }
}
