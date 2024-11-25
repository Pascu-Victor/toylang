package models.statements;

import models.PrgState;

public class NopStmt implements IStmt {
    public PrgState execute(PrgState state) {
        return null;
    }

    public String toString() {
        return "nop";
    }

    public NopStmt deepCopy() {
        return new NopStmt();
    }
}