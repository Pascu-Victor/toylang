package com.wmy.repo;

import java.util.List;

import com.wmy.exceptions.ExecutionException;
import com.wmy.models.PrgState;

import javafx.collections.ObservableList;

public interface IStateRepo {
    void add(PrgState elem);

    void remove(PrgState elem);

    boolean contains(PrgState elem);

    String toString();

    void logPrgStateExec(PrgState state) throws ExecutionException;

    List<PrgState> getPrgList();

    ObservableList<PrgState> getObsPrgList();

    void setPrgList(List<PrgState> states);

    void triggerChange();
}