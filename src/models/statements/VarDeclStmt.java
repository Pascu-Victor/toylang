package models.statements;

import exceptions.ExecutionException;
import models.PrgState;
import models.types.IType;

public class VarDeclStmt implements IStmt {
    private String name;
    private IType type;
    public VarDeclStmt(String name, IType type) {
        this.name = name;
        this.type = type;
    }    

    public PrgState execute(PrgState state) throws ExecutionException {
        state.getSymTable().set(name, type.defaultValue());
        return null;
    }

    public String toString() {
        return "declare(" +type.toString() + " " + name + ")";
    }

    public VarDeclStmt deepCopy() {
        return new VarDeclStmt(name, type);
    }

}
