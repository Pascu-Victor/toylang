package com.wmy.models.statements;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableBufferedReader;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.expressions.IExp;
import com.wmy.models.types.IType;
import com.wmy.models.types.StringType;
import com.wmy.models.values.IValue;
import com.wmy.models.values.StringValue;

public class OpenRFileStmt implements IStmt{

    IExp filename;

    public OpenRFileStmt(IExp fileNameExp) {
        this.filename = fileNameExp;
    }

    public PrgState execute(PrgState state) throws ExecutionException {
        IValue val = filename.eval(state.getSymTable(), state.getHeap());
        if (val.getType() instanceof StringType) {
            CloneableString str = ((StringValue)val).getVal();
            if (state.getFileTable().containsKey(str)) {
                throw new ExecutionException("File already opened");
            }
            try { 
                var fb = new CloneableBufferedReader( new BufferedReader(
                    new FileReader(str.toString())
                ));

                state.getFileTable().set(str, fb);

            }
            catch (FileNotFoundException e) {
                throw new ExecutionException("File not found:" + str);
            }

        } else {
            throw new ExecutionException("Filename is not a string");
        }
        return null;
    }

    public IStmt deepCopy() {
        return new OpenRFileStmt(filename.deepCopy());
    }

    @Override
    public String toString() {
        return "openRFile(" + filename + ")";
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        var expType = filename.typecheck(typeEnvironment);
        if(!expType.equals(new StringType())) {
            throw new TypeException("OpenRFile: expected type StringType, got: " + expType);
        }
        return typeEnvironment;
    }
}