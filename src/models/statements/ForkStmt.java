package models.statements;

import java.util.concurrent.ExecutionException;

import models.PrgState;

public class ForkStmt implements IStmt {
    IStmt statement;
    public ForkStmt(IStmt s){
        this.statement=s;
    }
    public PrgState execute(PrgState state) throws exceptions.ExecutionException{
        return new PrgState(state.getExeStack(), state.getSymTable().deepCopy(), state.getOut(),state.getFileTable() , state.getHeap(), this.statement);
    }
    public String toString(){
        return "fork("+this.statement.toString()+")";
    }
    public IStmt deepCopy(){
        return new ForkStmt(this.statement.deepCopy());
    }
}
