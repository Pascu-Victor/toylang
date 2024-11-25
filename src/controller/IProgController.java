package controller;

import java.util.List;
import java.util.function.Consumer;

import exceptions.ExecutionException;
import models.PrgState;

public interface IProgController {
   
    public void allStep() throws ExecutionException;
    public void toggleDisplayState();
    public void setConsumer(Consumer<String> displayCallback) throws ExecutionException;
    List<PrgState> removeCompletedPrg(List<PrgState> lst);
}
