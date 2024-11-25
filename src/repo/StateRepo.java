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
    private int size;
    private int crtPrg;
    private String logFilePath;

    public StateRepo() {
        this.states = new ArrayList<>();
        this.size = 0;
        this.crtPrg = 0;
    }

    public StateRepo(PrgState state, String logFile) {
        this.states = new ArrayList<>();
        this.size = 1;
        this.crtPrg = 0;
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

    public List<PrgState> getPrgList() {
        return states;
    }

    public void setPrgList(List<PrgState> states) {
        this.states = states;
    }
}
