package models.statements;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import exceptions.ExecutionException;
import models.PrgState;
import models.expressions.IExp;
import models.types.StringType;
import models.values.IValue;
import models.values.StringValue;

public class OpenRFileStmt implements IStmt{

    IExp filename;

    public OpenRFileStmt(IExp fileNameExp) {
        this.filename = fileNameExp;
    }

    public PrgState execute(PrgState state) throws ExecutionException {
        IValue val = filename.eval(state.getSymTable(), state.getHeap());
        if (val.getType() instanceof StringType) {
            String str = ((StringValue)val).getVal();
            if (state.getFileTable().containsKey(str)) {
                throw new ExecutionException("File already opened");
            }
            try { 
                var fb = new BufferedReader(
                    new FileReader(str)
                );

                state.getFileTable().set(str, fb);

            }
            catch (FileNotFoundException e) {
                throw new ExecutionException("File not found:" + str);
            }

        } else {
            throw new ExecutionException("Filename is not a string");
        }
        return state;
    }

    public IStmt deepCopy() {
        return new OpenRFileStmt(filename.deepCopy());
    }

    @Override
    public String toString() {
        return "openRFile(" + filename + ")";
    }
}