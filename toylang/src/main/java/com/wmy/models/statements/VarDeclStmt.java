package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.types.IType;

public class VarDeclStmt implements IStmt {
    private CloneableString name;
    private IType type;
    public VarDeclStmt(CloneableString name, IType type) {
        this.name = name;
        this.type = type;
    }

    public VarDeclStmt(String name, IType type) {
        this.name = new CloneableString(name);
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

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        typeEnvironment.set(name, type);
        return typeEnvironment;
    }
}
