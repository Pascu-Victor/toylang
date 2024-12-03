package controller;

import java.util.function.Consumer;

import exceptions.ExecutionException;

public interface IProgController {
    public void allStep() throws ExecutionException;
    public void toggleDisplayState();
    public void setConsumer(Consumer<String> displayCallback) throws ExecutionException;
}
