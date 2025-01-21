package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.types.IType;
import com.wmy.models.types.IntType;
import com.wmy.models.values.IntValue;

public class LockStmt implements IStmt {
    private CloneableString var;

    public LockStmt(CloneableString var) {
        this.var = var;
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        if (!typeEnvironment.containsKey(var)) {
            throw new TypeException("Lock variable " + var + " not declared");
        }
        if (!typeEnvironment.get(var).equals(new IntType())) {
            throw new TypeException("Lock variable " + var + " is not an integer");
        }
        return typeEnvironment;
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        var lockTable = state.getLockTable();
        synchronized (lockTable) {
            if (!state.getSymTable().containsKey(var)) {
                throw new ExecutionException("Lock variable " + var + " not declared");
            }
            var lockVar = state.getSymTable().get(var);
            if (!lockVar.getType().equals(new IntType())) {
                throw new ExecutionException("Lock variable " + var + " is not an integer");
            }
            var lockNum = ((IntValue) lockVar).getVal();
            if (!lockTable.contains(lockNum)) {
                throw new ExecutionException("Lock variable " + var + " id: " + lockNum + " not found in LockTable");
            }
            if (lockTable.at(lockNum) == -1) {
                lockTable.set(lockNum, state.getId());
            } else if (lockTable.at(lockNum) != state.getId()) {
                state.getExeStack().push(this);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "lock(" + var + ")";
    }

    @Override
    public IStmt deepCopy() {
        return new LockStmt(var.deepCopy());
    }

}
