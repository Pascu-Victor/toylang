package repo;

import exceptions.ExecutionException;
import models.PrgState;

public interface IStateRepo {
    void add(PrgState elem);
    void remove(PrgState elem);
    boolean contains(PrgState elem);
    PrgState getCrtPrg();
    PrgState[] getAll();
    String toString();
    void logPrgStateExec() throws ExecutionException;
}