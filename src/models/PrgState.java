package models;

import models.adt.ADict;
import models.adt.AList;
import models.adt.AStack;
import models.statements.IStmt;
import models.values.IValue;

public class PrgState {
    private AStack<IStmt> exeStack;

    
    private ADict<String, IValue> symTable;
    
    private AList<IValue> out;
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

    public void setExeStack(AStack<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public void setSymTable(ADict<String, IValue> symTable) {
        this.symTable = symTable;
    }

    public void setOut(AList<IValue> out) {
        this.out = out;
    }

    public void setOriginalProgram(IStmt originalProgram) {
        this.originalProgram = originalProgram;
    }

    public PrgState(AStack<IStmt> exeStack, ADict<String, IValue> symTable, AList<IValue> out, IStmt program) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.originalProgram = program.deepCopy();
        exeStack.push(program);
    }

    @Override
    public String toString() {
        return "ExeStack: " + exeStack.toString() + "\nSymTable: " + symTable.toString() + "\nOut: " + out.toString();
    }
}
