package com.wmy.models.statements;

import java.io.IOException;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.expressions.IExp;
import com.wmy.models.types.IType;
import com.wmy.models.types.IntType;
import com.wmy.models.types.StringType;
import com.wmy.models.values.IValue;
import com.wmy.models.values.IntValue;
import com.wmy.models.values.StringValue;

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
    
    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        var typeExp = fileName.typecheck(typeEnvironment);
        if (!typeExp.equals(new StringType())) {
            throw new TypeException("File name must be a string");
        }
        if (!typeEnvironment.get(varName).equals(new IntType())) {
            throw new TypeException("Variable must be an integer");
        }
        return typeEnvironment;
    }
}
