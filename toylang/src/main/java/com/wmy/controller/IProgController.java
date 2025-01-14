package com.wmy.controller;

import java.util.function.Consumer;

import com.wmy.exceptions.ExecutionException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableBufferedReader;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.adt.IHeap;
import com.wmy.models.adt.IList;
import com.wmy.models.adt.IStack;
import com.wmy.models.statements.IStmt;
import com.wmy.models.values.IValue;

import javafx.collections.ObservableList;

public interface IProgController {
    public void allStep() throws ExecutionException;

    public void toggleDisplayState();

    public void setConsumer(Consumer<String> displayCallback) throws ExecutionException;

    public void runOneStep() throws ExecutionException;

    public ObservableList<PrgState> getProgramStateList();

    public void setSelectedProgramState(PrgState state);

    public PrgState getSelectedProgramState();

    public IStack<IStmt> getSelectedProgramStateExeStack();

    public IDict<CloneableString, IValue> getSelectedProgramStateSymTable();

    public IHeap getSelectedProgramStateHeapTable();

    public IList<IValue> getSelectedProgramStateOutput();

    public IDict<CloneableString, CloneableBufferedReader> getSelectedProgramStateFileTable();

}
