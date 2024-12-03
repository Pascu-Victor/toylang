package models.statements;

import exceptions.ExecutionException;
import exceptions.TypeException;
import models.PrgState;
import models.adt.CloneableString;
import models.adt.IDict;
import models.expressions.IExp;
import models.types.IType;
import models.types.StringType;
import models.values.StringValue;

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
