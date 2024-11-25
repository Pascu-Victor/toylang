package repo;

import java.util.List;

import exceptions.ExecutionException;
import models.PrgState;

public interface IStateRepo {
    void add(PrgState elem);
    void remove(PrgState elem);
    boolean contains(PrgState elem);
    String toString();
    List<PrgState> getPrgList();
    void setPrgList(List<PrgState> states);
    void logPrgStateExec(PrgState state) throws ExecutionException;
}