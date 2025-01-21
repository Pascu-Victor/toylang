package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.types.IType;
import com.wmy.models.types.IntType;
import com.wmy.models.values.IntValue;

public class NewLockStmt implements IStmt {
    private CloneableString var;

    public NewLockStmt(CloneableString var) {
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
            var lockNum = lockTable.add(-1);
            if (!state.getSymTable().containsKey(var)) {
                throw new ExecutionException("Lock variable " + var + " not declared");
            }
            if (!state.getSymTable().get(var).getType().equals(new IntType())) {
                throw new ExecutionException("Lock variable " + var + " is not an integer");
            }
            state.getSymTable().set(var, new IntValue(lockNum));
        }
        return null;
    }

    @Override
    public String toString() {
        return "newLock(" + var + ")";
    }

    @Override
    public IStmt deepCopy() {
        return new NewLockStmt(var.deepCopy());
    }

}
