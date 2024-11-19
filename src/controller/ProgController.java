package controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    Map<Integer, IHeap.HeapEntry> safeGarbageCollector(List<Integer> addrsToKeep, IHeap heap){
        return heap.entrySet().stream()
        .filter(e->addrsToKeep.contains(e.getKey()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    List<Integer> getAddr(Collection<IValue> symTableValues, IHeap heap){
        return symTableValues.stream()
        .filter(v->v instanceof RefValue)
        .filter(v->((RefValue)v).getAddr()!=0)
        .filter(v->heap.contains(((RefValue)v).getAddr()))
        .map(v->{
            RefValue v1 = (RefValue)v;
            return getRefsRec(v1.getAddr(), heap);
        })
        .flatMap(e -> e)
        .collect(Collectors.toList());
    }

    private Stream<Integer> getRefsRec(int addr, IHeap heap){
        try {
            var v = heap.at(addr);
            if(v.getType() instanceof RefType){
                return Stream.concat(
                    getRefsRec(((RefValue)v).getAddr(), heap),
                    Stream.of(addr)
                );
            }
        } catch (ExecutionException e) {

        }
        return Stream.of(addr);
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
                safeGarbageCollector(
                    getAddr(prog.getSymTable().values(),prog.getHeap()),
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
