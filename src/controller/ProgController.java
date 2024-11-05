package controller;

import java.util.function.Consumer;

import exceptions.ExecutionException;
import models.PrgState;
import repo.IStateRepo;

public class ProgController implements IProgController {
    IStateRepo repo;
    Consumer<String> callback;
    boolean display = false;

    public ProgController(IStateRepo repo) {
        this.repo = repo;
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
