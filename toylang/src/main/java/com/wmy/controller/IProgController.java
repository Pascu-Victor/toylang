package com.wmy.controller;

import java.util.function.Consumer;

import com.wmy.exceptions.ExecutionException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableBufferedReader;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IBarrierTable;
import com.wmy.models.adt.IDict;
import com.wmy.models.adt.IHeap;
import com.wmy.models.adt.ILatchTable;
import com.wmy.models.adt.IList;
import com.wmy.models.adt.ILockTable;
import com.wmy.models.adt.IProcTable;
import com.wmy.models.adt.ISemaphoreTable;
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

    public ILatchTable getSelectedProgramStateLatchTable();

    public IProcTable getSelectedProgramStateProcedureTable();

    public ISemaphoreTable getSelectedProgramStateSemaphoreTable();

    public ILockTable getSelectedProgramStateLockTable();

    public IBarrierTable getSelectedProgramStateBarrierTable();
}
