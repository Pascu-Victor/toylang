package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.types.IType;
import com.wmy.models.types.IntType;
import com.wmy.models.values.IValue;
import com.wmy.models.values.IntValue;

public class SemAquireStmt implements IStmt {
    private CloneableString var;

    public SemAquireStmt(CloneableString var) {
        this.var = var;
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        var type = typeEnvironment.get(var);
        if (!type.equals(new IntType())) {
            throw new TypeException("Semaphore variable must be of type int");
        }
        return typeEnvironment;
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        IValue index = state.getSymTable().get(var);
        if (!index.getType().equals(new IntType())) {
            throw new ExecutionException("Semaphore index must be of type int");
        }
        int semIndex = ((IntValue) index).getVal();

        var semTable = state.getSemaphoreTable();

        synchronized (semTable) {
            var entry = semTable.at(semIndex);
            var semValue = entry.getKey();
            var semList = entry.getValue();

            if (semList.size() < semValue) {
                if (!semList.contains(state.getId())) {
                    semList.add(state.getId());
                }
            } else {
                state.getExeStack().push(this);
            }
        }

        return null;
    }

    public SemAquireStmt deepCopy() {
        return new SemAquireStmt(var.deepCopy());
    }

    @Override
    public String toString() {
        return "acquire(" + var + ")";
    }
}
