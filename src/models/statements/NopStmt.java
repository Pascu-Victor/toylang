package models.statements;

import exceptions.TypeException;
import models.PrgState;
import models.adt.CloneableString;
import models.adt.IDict;
import models.types.IType;

public class NopStmt implements IStmt {
    public PrgState execute(PrgState state) {
        return null;
    }

    public String toString() {
        return "nop";
    }

    public NopStmt deepCopy() {
        return new NopStmt();
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        return typeEnvironment;
    }
}