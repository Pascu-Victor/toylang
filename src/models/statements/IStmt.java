package models.statements;

import java.util.concurrent.ExecutionException;

import models.PrgState;

public interface IStmt {
    PrgState execute(PrgState state) throws ExecutionException, exceptions.ExecutionException;
    IStmt deepCopy();
}
