package repo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import exceptions.ExecutionException;
import models.PrgState;

public class StateRepo implements IStateRepo {
    private PrgState[] states;
    private int size;
    private int crtPrg;
    private String logFilePath;

    public StateRepo() {
        this.states = new PrgState[10];
        this.size = 0;
        this.crtPrg = 0;
    }

    public StateRepo(PrgState state, String logFile) {
        this.states = new PrgState[10];
        this.size = 1;
        this.crtPrg = 0;
        this.states[0] = state;
        this.logFilePath = logFile;
    }

    public void add(PrgState state) {
        if (size == states.length) {
            PrgState[] newStates = new PrgState[states.length * 2];
            System.arraycopy(states, 0, newStates, 0, states.length);
            states = newStates;
        }
        states[size++] = state;
    }

    public void remove(PrgState state) {
        for (int i = 0; i < size; i++) {
            if (states[i] == state) {
                System.arraycopy(states, i + 1, states, i, size - i - 1);
                size--;
                break;
            }
        }
    }

    public boolean contains(PrgState state) {
        for (int i = 0; i < size; i++) {
            if (states[i] == state) {
                return true;
            }
        }
        return false;
    }

    public PrgState getCrtPrg() {
        return states[this.crtPrg];
    }

    public PrgState[] getAll() {
        PrgState[] allStates = new PrgState[size];
        System.arraycopy(states, 0, allStates, 0, size);
        return allStates;
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

    public void logPrgStateExec() throws ExecutionException {
        PrgState crtPrg = getCrtPrg();
        try {
            var logFile = new PrintWriter(new BufferedWriter(new FileWriter(this.logFilePath, true)));
            logFile.println(crtPrg.toString());
            logFile.flush();
            logFile.close();
        }
        catch (IOException e) {
            throw new ExecutionException("Could not open log file: " + e.getMessage());
        }
    }
}
