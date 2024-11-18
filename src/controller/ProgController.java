package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import exceptions.ExecutionException;
import models.PrgState;
import models.adt.IHeap;
import models.types.RefType;
import models.values.IValue;
import models.values.RefValue;
import repo.IStateRepo;

public class ProgController implements IProgController {
    IStateRepo repo;
    Consumer<String> callback;
    boolean display = false;

    public ProgController(IStateRepo repo) {
        this.repo = repo;
    }

    Map<Integer, IHeap.HeapEntry> unsafeGarbageCollector(List<Integer> symTableAddr, IHeap heap){
        System.err.println(symTableAddr);
        return heap.entrySet().stream()
        .filter(e->symTableAddr.contains(e.getKey()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues){
        return symTableValues.stream()
        .filter(v->v instanceof RefValue)
        .map(v->{
            RefValue v1 = (RefValue)v;
            return v1.getAddr();
        })
        .collect(Collectors.toList());
    }

    private List<Integer> getRefsToRefsHeap(Collection<IValue> symTableValues, Heap heap){
        //keep values that are referenced in the symbol table or indirectly referenced in the heap by a reference in the symbol table
        return symTableValues.stream()
        .filter(v->v instanceof RefValue)
        .map(v->{
            RefValue v1 = (RefValue)v;
            return v1.getAddr();
        })
        .filter(addr->heap.contains(addr))
        .map(addr->{
            IValue v1 = heap.at(addr);
            if(v1 instanceof RefValue){
                RefValue v2 = (RefValue)v1;
                return v2.getAddr();
            }
            return -1;
        })
        .filter(addr->addr!=-1)
        .collect(Collectors.toList());
    }
        

    public PrgState oneStep(PrgState state) throws ExecutionException {
        var stk = state.getExeStack();
        if(stk.isEmpty()) throw new ExecutionException("Program stack is empty");
        var crtStmt = stk.pop();
        return crtStmt.execute(state);
    }

    public void allStep() throws ExecutionException {
        var prog = repo.getCrtPrg();
        repo.logPrgStateExec();
        while (!prog.getExeStack().isEmpty()) {
            this.oneStep(prog);
            repo.logPrgStateExec();
            prog.getHeap().setContent(
                unsafeGarbageCollector(
                    getAddrFromSymTable(prog.getSymTable().values()),
                    prog.getHeap()
                )
            );
            repo.logPrgStateExec();
        }
    }

    public void toggleDisplayState() {
        this.display = !this.display;
    }

    public void setConsumer(Consumer<String> displayCallback) throws ExecutionException {
        if(callback != null) {
            throw new ExecutionException("Consumer already set");
        }
        this.callback = displayCallback;
    }
}
