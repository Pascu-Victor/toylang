package models.statements;

import exceptions.ExecutionException;
import models.PrgState;

public interface IStmt {
    PrgState execute(PrgState state) throws ExecutionException;
    IStmt deepCopy();
}
