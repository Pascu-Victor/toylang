package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.types.IType;
import com.wmy.models.types.IntType;
import com.wmy.models.values.IntValue;
import com.wmy.models.values.StringValue;

public class CountDownStmt implements IStmt {
    private CloneableString var;

    public CountDownStmt(CloneableString var) {
        this.var = var;
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        if (!typeEnvironment.containsKey(var)) {
            throw new TypeException("Variable " + var + " not declared");
        }

        var type = typeEnvironment.get(var);
        if (!type.equals(new IntType())) {
            throw new TypeException("Latch variable " + var + " is not of type int");
        }

        return typeEnvironment;
    }

    @Override
    public IStmt deepCopy() {
        return new CountDownStmt(var.deepCopy());
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        var symTable = state.getSymTable();
        var varValue = symTable.get(var);
        if (!varValue.getType().equals(new IntType())) {
            throw new ExecutionException("Variable " + var + " is not of type int");
        }

        var latchKey = ((IntValue) varValue).getVal();
        var latchTable = state.getLatchTable();
        synchronized (latchTable) {
            if (!latchTable.contains(latchKey)) {
                throw new ExecutionException("Invalid latch key " + latchKey);
            }

            var latchValue = latchTable.at(latchKey);
            if (latchValue > 0) {
                latchTable.set(latchKey, latchValue - 1);
                state.getOut().add(new StringValue("Prg:" + state.getId() + " decremented latch " + latchKey));
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "countDown(" + var + ")";
    }

}
