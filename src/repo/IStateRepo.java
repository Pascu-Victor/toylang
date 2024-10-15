package repo;

import models.PrgState;

public interface IStateRepo {
    void add(PrgState elem);
    void remove(PrgState elem);
    boolean contains(PrgState elem);
    PrgState get(int index);
    PrgState[] getAll();
    String toString();
}