package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.types.IType;
import com.wmy.models.types.IntType;
import com.wmy.models.values.IntValue;

public class BarrierAwaitStmt implements IStmt {
    private CloneableString var;

    public BarrierAwaitStmt(CloneableString var) {
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        var barrierTable = state.getBarrierTable();
        synchronized (barrierTable) {
            if (!state.getSymTable().containsKey(var)) {
                throw new ExecutionException("Barrier variable " + var + " not declared");
            }
            if (!state.getSymTable().get(var).getType().equals(new IntType())) {
                throw new ExecutionException("Barrier variable " + var + " not of type int");
            }
            var barrierValue = state.getSymTable().get(var);
            var barrierId = ((IntValue) barrierValue).getVal();
            if (!barrierTable.contains(barrierId)) {
                throw new ExecutionException("Barrier " + barrierId + " not found in BarrierTable");
            }
            var barrierEntry = barrierTable.at(barrierId);
            synchronized (barrierEntry) {
                if (barrierEntry.getCount() > barrierEntry.getWaitList().size()) {
                    if (!barrierEntry.getWaitList().contains(state.getId())) {
                        barrierEntry.getWaitList().add(state.getId());
                    }
                    state.getExeStack().push(this);
                }
            }
        }
        return null;
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        if (!typeEnvironment.containsKey(var)) {
            throw new TypeException("Barrier variable " + var + " not declared");
        }
        if (!typeEnvironment.get(var).equals(new IntType())) {
            throw new TypeException("Barrier variable " + var + " not of type int");
        }
        return typeEnvironment;
    }

    @Override
    public IStmt deepCopy() {
        return new BarrierAwaitStmt(var.deepCopy());
    }

    @Override
    public String toString() {
        return "barrierAwait(" + var + ")";
    }

}
