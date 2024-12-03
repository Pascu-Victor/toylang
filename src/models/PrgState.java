package models;

import java.util.stream.Collectors;

import exceptions.ExecutionException;
import models.adt.ADict;
import models.adt.AList;
import models.adt.AStack;
import models.adt.CloneableBufferedReader;
import models.adt.CloneableString;
import models.adt.Heap;
import models.adt.IDict;
import models.adt.IHeap;
import models.adt.IList;
import models.adt.IStack;
import models.statements.IStmt;
import models.types.IntType;
import models.values.IValue;

public class PrgState {
    private IStack<IStmt> exeStack;
    
    private IDict<CloneableString, IValue> symTable;
    
    private IList<IValue> out;

    private IDict<CloneableString, CloneableBufferedReader> fileTable;

    private IHeap heap;

    private int id;
    private static int nextId = 0;

    IStmt originalProgram;
    
    public IStack<IStmt> getExeStack() {
        return exeStack;
    }

    public IDict<CloneableString, IValue> getSymTable() {
        return symTable;
    }

    public IList<IValue> getOut() {
        return out;
    }

    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    public IHeap getHeap() {
        return heap;
    }

    public void setExeStack(AStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public void setSymTable(ADict<CloneableString, IValue> symTable) {
        this.symTable = symTable;
    }

    public void setOut(AList<IValue> out) {
        this.out = out;
    }

    public void setHeap(Heap heap) {
        this.heap = heap;
    }

    public void setOriginalProgram(IStmt originalProgram) {
        this.originalProgram = originalProgram;
    }

    public IDict<CloneableString, CloneableBufferedReader> getFileTable() {
        return fileTable;
    }

    public void setFileTable(ADict<CloneableString, CloneableBufferedReader> fileTable) {
        this.fileTable = fileTable;
    }

    public static synchronized int getNextId() {
        return ++PrgState.nextId;
    }

    public PrgState(IStack<IStmt> exeStack, IDict<CloneableString, IValue> symTable, IList<IValue> out, IDict<CloneableString, CloneableBufferedReader> fbs, IHeap heap, IStmt program) {
        this.id = PrgState.getNextId();
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.originalProgram = program.deepCopy();
        this.fileTable = fbs;
        this.heap = heap;
        exeStack.push(program);
    }

    public boolean isNotCompleted() {
        return !exeStack.isEmpty();
    }

    public PrgState oneStep() throws ExecutionException {
        var stk = this.getExeStack();
        if(stk.isEmpty()) throw new ExecutionException("Program stack is empty");
        var crtStmt = stk.pop();
        return crtStmt.execute(this);
    }

    @Override
    public String toString() {
        var s = new StringBuilder();
        
        var exeList = exeStack.stream().collect(Collectors.toList());

        for(IStmt stmt : exeList.reversed()) {
            s.append(stmt + "\n");
        }

        return
        "State id:"
        + Integer.toString(this.id)
        +"\nExeStack:\n"
        + s.toString()
        + "SymTable:\n"
        + symTable.toString()
        + "\nOut:\n"
        + out.toString()
        + "File Table:\n"
        + fileTable.keys().toString()
        + "Heap:\n"
        + heap.toString()
        + "\n";
    }
}
