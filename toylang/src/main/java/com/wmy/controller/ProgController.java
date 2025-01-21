package com.wmy.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.wmy.exceptions.ExecutionException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableBufferedReader;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.adt.IHeap;
import com.wmy.models.adt.ILatchTable;
import com.wmy.models.adt.IList;
import com.wmy.models.adt.IProcTable;
import com.wmy.models.adt.ISemaphoreTable;
import com.wmy.models.adt.IStack;
import com.wmy.models.statements.IStmt;
import com.wmy.models.types.RefType;
import com.wmy.models.values.IValue;
import com.wmy.models.values.RefValue;
import com.wmy.repo.IStateRepo;

import javafx.collections.ObservableList;

public class ProgController implements IProgController {
    IStateRepo repo;
    Consumer<String> callback;
    boolean display = false;
    ExecutorService executor;
    PrgState selectedPrgState;

    public ProgController(IStateRepo repo) {
        this.repo = repo;
    }

    public ProgController() {

    }

    Map<Integer, IHeap.HeapEntry> safeGarbageCollector(List<Integer> addrsToKeep, IHeap heap) {
        return heap.entrySet().stream()
                .filter(e -> addrsToKeep.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    List<Integer> getAddr(Collection<IValue> symTableValues, IHeap heap) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .filter(v -> ((RefValue) v).getAddr() != 0)
                .filter(v -> heap.contains(((RefValue) v).getAddr()))
                .map(v -> {
                    RefValue v1 = (RefValue) v;
                    return getRefsRec(v1.getAddr(), heap);
                })
                .flatMap(e -> e)
                .collect(Collectors.toList());
    }

    private Stream<Integer> getRefsRec(int addr, IHeap heap) {
        try {
            var v = heap.at(addr);
            if (v.getType() instanceof RefType) {
                return Stream.concat(
                        getRefsRec(((RefValue) v).getAddr(), heap),
                        Stream.of(addr));
            }
        } catch (ExecutionException e) {

        }
        return Stream.of(addr);
    }

    private void runGcOnAll(List<PrgState> states) {
        List<IValue> allSymbols = states.stream()
                .map(s -> s.getSymTable().values().stream())
                .flatMap(s -> s)
                .toList();
        states.getFirst().getHeap().setContent(
                safeGarbageCollector(
                        getAddr(allSymbols, states.getFirst().getHeap()),
                        states.getFirst().getHeap()));
    }

    List<PrgState> removeCompletedPrg(List<PrgState> states) {
        return states.stream().filter(s -> s.isNotCompleted()).toList();
    }

    public void oneStepForAllPrg(List<PrgState> states) throws ExecutionException {
        states.forEach(s -> {
            try {
                repo.logPrgStateExec(s);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        List<Callable<PrgState>> callList = states.stream()
                .map(s -> ((Callable<PrgState>) (() -> s.oneStep())))
                .toList();

        try {
            List<PrgState> newPrgStates = executor.invokeAll(callList).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(s -> s != null)
                    .toList();

            states = Stream.concat(
                    states.stream(),
                    newPrgStates.stream()).toList();
            states.forEach(s -> {
                try {
                    repo.logPrgStateExec(s);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
            repo.setPrgList(states);
        } catch (InterruptedException e) {
            throw new ExecutionException("Interrupted" + e.toString());
        }
    }

    public void allStep() throws ExecutionException {
        this.executor = Executors.newFixedThreadPool(2);
        // repo.logPrgStateExec();
        List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());
        while (!prgList.isEmpty()) {
            runGcOnAll(prgList);
            oneStepForAllPrg(prgList);
            prgList = removeCompletedPrg(repo.getPrgList());
        }
        executor.shutdownNow();
        repo.setPrgList(prgList);
    }

    public void runOneStep() throws ExecutionException {
        if (executor == null) {
            executor = Executors.newFixedThreadPool(2);
        }
        List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());
        if (prgList.isEmpty()) {
            executor.shutdownNow();
            return;
        }
        runGcOnAll(prgList);
        oneStepForAllPrg(prgList);
        repo.triggerChange();
    }

    public void toggleDisplayState() {
        this.display = !this.display;
    }

    public void setConsumer(Consumer<String> displayCallback) throws ExecutionException {
        if (callback != null) {
            throw new ExecutionException("Consumer already set");
        }
        this.callback = displayCallback;
    }

    public ObservableList<PrgState> getProgramStateList() {
        return repo.getObsPrgList();
    }

    public void setSelectedProgramState(PrgState state) {
        this.selectedPrgState = state;
    }

    public IHeap getSelectedProgramStateHeapTable() {
        return selectedPrgState.getHeap();
    }

    public IStack<IStmt> getSelectedProgramStateExeStack() {
        return selectedPrgState.getExeStack();
    }

    public IDict<CloneableString, IValue> getSelectedProgramStateSymTable() {
        return selectedPrgState.getSymTable();
    }

    public IList<IValue> getSelectedProgramStateOutput() {
        return selectedPrgState.getOut();
    }

    public IDict<CloneableString, CloneableBufferedReader> getSelectedProgramStateFileTable() {
        return selectedPrgState.getFileTable();
    }

    @Override
    public PrgState getSelectedProgramState() {
        return selectedPrgState;
    }

    public ILatchTable getSelectedProgramStateLatchTable() {
        return selectedPrgState.getLatchTable();
    }

    public IProcTable getSelectedProgramStateProcedureTable() {
        return selectedPrgState.getProcTable();
    }

    public ISemaphoreTable getSelectedProgramStateSemaphoreTable() {
        return selectedPrgState.getSemaphoreTable();
    }
}
