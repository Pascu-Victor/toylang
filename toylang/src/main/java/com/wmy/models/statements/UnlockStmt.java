package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.types.IType;
import com.wmy.models.types.IntType;
import com.wmy.models.values.IntValue;

public class UnlockStmt implements IStmt {
    private CloneableString var;

    public UnlockStmt(CloneableString var) {
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        var lockTable = state.getLockTable();
        synchronized (lockTable) {
            if (!state.getSymTable().containsKey(var)) {
                throw new ExecutionException("Unlock variable " + var + " not declared");
            }
            var lockVar = state.getSymTable().get(var);
            if (!lockVar.getType().equals(new IntType())) {
                throw new ExecutionException("Unlock variable " + var + " is not an integer");
            }
            var lockNum = ((IntValue) lockVar).getVal();
            if (!lockTable.contains(lockNum)) {
                throw new ExecutionException("Unlock variable " + var + " not found in LockTable");
            }
            if (lockTable.at(lockNum) == state.getId()) {
                lockTable.set(lockNum, -1);
            }
        }
        return null;
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        if (!typeEnvironment.containsKey(var)) {
            throw new TypeException("Unlock variable " + var + " not declared");
        }
        if (!typeEnvironment.get(var).equals(new IntType())) {
            throw new TypeException("Unlock variable " + var + " is not an integer");
        }
        return typeEnvironment;
    }

    @Override
    public IStmt deepCopy() {
        return new UnlockStmt(var.deepCopy());
    }

    @Override
    public String toString() {
        return "unlock(" + var + ")";
    }
}
