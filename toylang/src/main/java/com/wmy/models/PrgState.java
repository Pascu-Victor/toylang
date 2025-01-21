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
import com.wmy.models.adt.ILatchTable;
import com.wmy.models.adt.IList;
import com.wmy.models.adt.ILockTable;
import com.wmy.models.adt.IProcTable;
import com.wmy.models.adt.ISemaphoreTable;
import com.wmy.models.adt.IProcTable.ProcTableEntry;
import com.wmy.models.adt.IStack;
import com.wmy.models.statements.IStmt;
import com.wmy.models.values.IValue;

public class PrgState {
    private IStack<IStmt> exeStack;

    private IStack<IDict<CloneableString, IValue>> symTableStack;

    private IList<IValue> out;

    private IDict<CloneableString, CloneableBufferedReader> fileTable;

    private ILatchTable latchTable;

    private IProcTable procTable;

    private IHeap heap;

    private ISemaphoreTable semaphoreTable;

    private ILockTable lockTable;

    private int id;
    private static int nextId = 0;

    IStmt originalProgram;

    public IStack<IStmt> getExeStack() {
        return exeStack;
    }

    public IDict<CloneableString, IValue> getSymTable() {
        return symTableStack.top();
    }

    public IStack<IDict<CloneableString, IValue>> getSymTableStack() {
        return symTableStack;
    }

    public void popSymTable() {
        symTableStack.pop();
    }

    public void pushSymTable(IDict<CloneableString, IValue> symTable) {
        symTableStack.push(symTable);
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

    public ILatchTable getLatchTable() {
        return this.latchTable;
    }

    public IProcTable getProcTable() {
        return this.procTable;
    }

    public ILockTable getLockTable() {
        return this.lockTable;
    }

    public void setExeStack(AStack<IStmt> exeStack) {
        this.exeStack = exeStack;
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

    public void addProc(CloneableString name, ProcTableEntry entry) throws ExecutionException {
        this.procTable.set(name, entry);
    }

    public ISemaphoreTable getSemaphoreTable() {
        return semaphoreTable;
    }

    public PrgState(IStack<IStmt> exeStack, IStack<IDict<CloneableString, IValue>> symTableStack, IList<IValue> out,
            IDict<CloneableString, CloneableBufferedReader> fbs, IHeap heap, ILatchTable latchTable,
            IProcTable procTable, ISemaphoreTable semaphoreTable, ILockTable lockTable, IStmt program) {
        this.id = PrgState.getNextId();
        this.exeStack = exeStack;
        this.symTableStack = symTableStack;
        this.out = out;
        this.originalProgram = program.deepCopy();
        this.fileTable = fbs;
        this.heap = heap;
        this.latchTable = latchTable;
        this.procTable = procTable;
        this.semaphoreTable = semaphoreTable;
        this.lockTable = lockTable;
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
                + symTableStack.toString()
                + "\nOut:\n"
                + out.toString()
                + "File Table:\n"
                + fileTable.keys().toString()
                + "Heap:\n"
                + heap.toString()
                + "\nProcedure Table:\n"
                + procTable.toString()
                + "\nLatch Table:\n"
                + latchTable.toString()
                + "\nSemaphore Table:\n"
                + semaphoreTable.toString()
                + "\nLock Table:\n"
                + lockTable.toString()
                + "\n";
    }
}
