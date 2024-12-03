package models.statements;

import java.io.IOException;

import exceptions.ExecutionException;
import models.PrgState;
import models.adt.CloneableString;
import models.expressions.IExp;
import models.types.StringType;
import models.values.IValue;
import models.values.IntValue;
import models.values.StringValue;

public class ReadFileStmt implements IStmt {
    CloneableString varName;
    IExp fileName;

    public ReadFileStmt(IExp fileName, CloneableString varName) {
        this.varName = varName;
        this.fileName = fileName;
    }

    public ReadFileStmt(IExp fileName, String varName) {
        this.varName = new CloneableString(varName);
        this.fileName = fileName;
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        IValue val = fileName.eval(state.getSymTable(), state.getHeap());
        if (!val.getType().equals(new StringType())) {
            throw new ExecutionException("File name must be a string");
        }
        StringValue fileName = (StringValue) val;
        if (!state.getFileTable().containsKey(fileName.getVal())) {
            throw new ExecutionException("File does not exist");
        }
        try {
            CloneableString content = state.getFileTable().get(fileName.getVal()).readLine();
            IValue contentValue = new IntValue(Integer.parseInt(content.toString()));
            state.getSymTable().set(varName, contentValue);
        } catch (IOException e) {
            throw new ExecutionException("Error reading file");
        }
        catch (NumberFormatException e) {
            throw new ExecutionException("File content must be an integer");
        }
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new ReadFileStmt(fileName.deepCopy(), varName);
    }

    @Override
    public String toString() {
        return "readFile(" + varName + ", " + fileName + ")";
    }
    
}
