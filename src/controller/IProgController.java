package controller;

import java.util.function.Consumer;

import exceptions.ExecutionException;
import models.PrgState;

public interface IProgController {
    public PrgState oneStep(PrgState state) throws ExecutionException;
    public void allStep() throws ExecutionException;
    public void toggleDisplayState();
    public void setConsumer(Consumer<String> displayCallback) throws ExecutionException;
}
