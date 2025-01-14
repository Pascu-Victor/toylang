package com.wmy.models;

import java.util.stream.Collectors;

import com.wmy.exceptions.ExecutionException;
import com.wmy.models.adt.ADict;
import com.wmy.models.adt.AList;
import com.wmy.models.adt.AStack;
import com.wmy.models.adt.CloneableBufferedReader;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.Heap;
import com.wmy.models.adt.IDict;
import com.wmy.models.adt.IHeap;
import com.wmy.models.adt.IList;
import com.wmy.models.adt.IStack;
import com.wmy.models.statements.IStmt;
import com.wmy.models.values.IValue;

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

    public PrgState(IStack<IStmt> exeStack, IDict<CloneableString, IValue> symTable, IList<IValue> out,
            IDict<CloneableString, CloneableBufferedReader> fbs, IHeap heap, IStmt program) {
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
        if (stk.isEmpty())
            throw new ExecutionException("Program stack is empty");
        var crtStmt = stk.pop();
        return crtStmt.execute(this);
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        var s = new StringBuilder();

        var exeList = exeStack.stream().collect(Collectors.toList());

        for (IStmt stmt : exeList.reversed()) {
            s.append(stmt + "\n");
        }

        return "State id:"
                + Integer.toString(this.id)
                + "\nExeStack:\n"
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
