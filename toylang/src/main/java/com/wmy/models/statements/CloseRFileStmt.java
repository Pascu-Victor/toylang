package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.expressions.IExp;
import com.wmy.models.types.IType;
import com.wmy.models.types.StringType;
import com.wmy.models.values.StringValue;

public class CloseRFileStmt implements IStmt {
    IExp fileName;

    public CloseRFileStmt(IExp fileName) {
        this.fileName = fileName;
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        var val = fileName.eval(state.getSymTable(), state.getHeap());
        if (!val.getType().equals(new StringType())) {
            throw new ExecutionException("File name must be a string");
        }
        var fileName = (StringValue) val;
        if (!state.getFileTable().containsKey(fileName.getVal())) {
            throw new ExecutionException("File does not exist");
        }
        try {
            state.getFileTable().get(fileName.getVal()).close();
            state.getFileTable().remove(fileName.getVal());
        }
        catch (Exception e) {
            throw new ExecutionException("Error closing file");
        }
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new CloseRFileStmt(fileName.deepCopy());
    }

    @Override
    public String toString() {
        return "closeRFile(" + fileName.toString() + ")";
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        var expType = fileName.typecheck(typeEnvironment);
        if(!expType.equals(new StringType())) {
            throw new TypeException("CloseRFile: expected type StringType, got: " + expType);
        }
        return typeEnvironment;
    }
}
