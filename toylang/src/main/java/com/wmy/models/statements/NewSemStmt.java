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

public class NewSemStmt implements IStmt {
    private CloneableString varName;
    private IExp sizeExp;

    public NewSemStmt(CloneableString varName, IExp sizeExp) {
        this.varName = varName;
        this.sizeExp = sizeExp;
    }

    @Override
    public String toString() {
        return "newSemaphore(" + varName + ", " + sizeExp + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        var semTable = state.getSemaphoreTable();
        var value = sizeExp.eval(state.getSymTable(), state.getHeap());

        if (!value.getType().equals(new IntType())) {
            throw new RuntimeException("Semaphore size must be of type int");
        }

        var index = semTable.add(((IntValue) value).getVal(), new AList<Integer>());

        state.getSymTable().set(varName, new IntValue(index));
        return null;
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        var type = sizeExp.typecheck(typeEnvironment);
        if (!type.equals(new IntType())) {
            throw new TypeException("Semaphore size must be of type int");
        }

        if (!typeEnvironment.containsKey(varName)) {
            throw new TypeException("Semaphore variable must be declared");
        }

        var varType = typeEnvironment.get(varName);
        if (!varType.equals(new IntType())) {
            throw new TypeException("Semaphore variable must be of type int");
        }

        return typeEnvironment;
    }

    @Override
    public IStmt deepCopy() {
        return new NewSemStmt(varName.deepCopy(), sizeExp.deepCopy());
    }
}