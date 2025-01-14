package com.wmy.repo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.wmy.exceptions.ExecutionException;
import com.wmy.models.PrgState;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StateRepo implements IStateRepo {
    private ObservableList<PrgState> states;
    private String logFilePath;

    public StateRepo() {
        this.states = FXCollections.observableArrayList();
    }

    public StateRepo(PrgState state, String logFile) {
        this.states = FXCollections.observableArrayList();
        this.states.add(state);
        this.logFilePath = logFile;
    }

    public void add(PrgState state) {
        states.add(state);
    }

    public void remove(PrgState state) {
        states.remove(state);
    }

    public boolean contains(PrgState state) {
        return this.states.contains(state);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (PrgState state : this.states) {
            sb.append(state.toString());
            sb.append("\n");
        }

        return sb.toString();
    }

    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    public void logPrgStateExec(PrgState state) throws ExecutionException {
        try {
            var logFile = new PrintWriter(new BufferedWriter(new FileWriter(this.logFilePath, true)));
            logFile.println(state.toString());
            logFile.flush();
            logFile.close();
        } catch (IOException e) {
            throw new ExecutionException("Could not open log file: " + e.getMessage());
        }
    }

    public List<PrgState> getPrgList() {
        return states;
    }

    public void setPrgList(List<PrgState> states) {
        this.states.setAll(states);
    }

    public ObservableList<PrgState> getObsPrgList() {
        return this.states;
    }

    public void triggerChange() {
        if (this.states.size() > 0)
            this.states.set(0, this.states.get(0));
    }
}
