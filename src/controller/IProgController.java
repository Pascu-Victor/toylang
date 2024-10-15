package controller;

import exceptions.ExecutionException;
import models.PrgState;

public interface IProgController {
    public PrgState oneStep() throws ExecutionException;
    public PrgState allStep() throws ExecutionException;
    public void toggleDisplayFlag();
}
