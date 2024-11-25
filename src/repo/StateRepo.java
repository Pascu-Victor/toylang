package repo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import exceptions.ExecutionException;
import models.PrgState;

public class StateRepo implements IStateRepo {
    private List<PrgState> states;
    private String logFilePath;

    public StateRepo() {
        this.states = new ArrayList<>();
    }

    public StateRepo(PrgState state, String logFile) {
        this.states = new ArrayList<>();
        this.states.add(state);
        this.logFilePath = logFile;
    }

    public void add(PrgState state) {
        this.states.add(state);
    }

    public void remove(PrgState state) {
        this.states.remove(state);
    }

    public boolean contains(PrgState state) {
        return this.states.contains(state);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(PrgState state : this.states) {
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
        }
        catch (IOException e) {
            throw new ExecutionException("Could not open log file: " + e.getMessage());
        }
    }
    @Override
    public List<PrgState> getPrgList() {
        return this.states;
    }

    @Override
    public void setPrgList(List<PrgState> states) {
        this.states = states;
    }
    
}
