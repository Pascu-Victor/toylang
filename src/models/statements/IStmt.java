package models.statements;

import exceptions.ExecutionException;
import exceptions.TypeException;
import models.PrgState;
import models.adt.CloneableString;
import models.adt.IDict;
import models.types.IType;

public interface IStmt {
    PrgState execute(PrgState state) throws ExecutionException;
    IStmt deepCopy();
    IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException;
}
