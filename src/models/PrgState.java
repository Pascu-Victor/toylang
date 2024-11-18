package models;

import java.io.BufferedReader;
import java.util.stream.Collectors;

import models.adt.ADict;
import models.adt.AList;
import models.adt.AStack;
import models.adt.Heap;
import models.statements.IStmt;
import models.values.IValue;

public class PrgState {
    private AStack<IStmt> exeStack;
    
    private ADict<String, IValue> symTable;
    
    private AList<IValue> out;

    private ADict<String, BufferedReader> fileTable;

    private Heap heap;

    IStmt originalProgram;
    
    public AStack<IStmt> getExeStack() {
        return exeStack;
    }

    public ADict<String, IValue> getSymTable() {
        return symTable;
    }

    public AList<IValue> getOut() {
        return out;
    }

    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    public Heap getHeap() {
        return heap;
    }

    public void setExeStack(AStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public void setSymTable(ADict<String, IValue> symTable) {
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

    public ADict<String, BufferedReader> getFileTable() {
        return fileTable;
    }

    public void setFileTable(ADict<String, BufferedReader> fileTable) {
        this.fileTable = fileTable;
    }

    public PrgState(AStack<IStmt> exeStack, ADict<String, IValue> symTable, AList<IValue> out, ADict<String, BufferedReader> fbs, Heap heap, IStmt program) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.originalProgram = program.deepCopy();
        this.fileTable = fbs;
        this.heap = heap;
        exeStack.push(program);
    }

    @Override
    public String toString() {
        var s = new StringBuilder();
        
        var exeList = exeStack.stream().collect(Collectors.toList());

        for(IStmt stmt : exeList.reversed()) {
            s.append(stmt + "\n");
        }

        return
        "ExeStack:\n"
        + s.toString()
        + "SymTable:\n"
        + symTable.toString()
        + "Out:\n"
        + out.toString()
        + "\nFile Table:\n"
        + fileTable.keys().toString()
        + "\nHeap:\n"
        + heap.toString();
    }
}
