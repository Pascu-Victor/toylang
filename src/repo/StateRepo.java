package repo;

import models.PrgState;

public class StateRepo implements IStateRepo {
    private PrgState[] states;
    private int size;

    public StateRepo() {
        states = new PrgState[10];
        size = 0;
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

    public PrgState get(int index) {
        return states[index];
    }

    public PrgState[] getAll() {
        PrgState[] allStates = new PrgState[size];
        System.arraycopy(states, 0, allStates, 0, size);
        return allStates;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(states[i].toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
