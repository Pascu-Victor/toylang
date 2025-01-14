package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.expressions.IExp;
import com.wmy.models.types.IType;
import com.wmy.models.types.RefType;
import com.wmy.models.values.IValue;
import com.wmy.models.values.RefValue;

public class NewStmt implements IStmt {
    private CloneableString varName;
    private IExp exp;

    public NewStmt(CloneableString varName, IExp exp) {
        this.varName = varName;
        this.exp = exp;
    }

    public NewStmt(String varName, IExp exp) {
        this.varName = new CloneableString(varName);
        this.exp = exp;
    }

    public CloneableString getVarName() {
        return varName;
    }

    public IExp getExp() {
        return exp;
    }

    @Override
    public String toString() {
        return "new(" + varName + "," + exp + ")";
    }

    public IStmt deepCopy() {
        return new NewStmt(varName.deepCopy(), exp.deepCopy());
    }

    public PrgState execute(PrgState state) throws ExecutionException {
        IValue val = exp.eval(state.getSymTable(), state.getHeap());
        IValue ref = state.getSymTable().get(varName);
        if (!ref.getType().equals(new RefType(val.getType()))) {
            throw new ExecutionException("Variable " + varName + " is not a ref to " + val.getType());
        }

        int addr = state.getHeap().add(val);
        var type = ((RefType)ref.getType()).getInner();
        state.getSymTable().set(varName, new RefValue(addr, type));
        
        return null;
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        var expType = exp.typecheck(typeEnvironment);
        var varType = typeEnvironment.get(varName);

        if(!varType.equals(new RefType(expType))) {
            throw new TypeException("NewStmt: invalid assignment of exp: " + expType + " to heap ref: " + varType);
        }
        return typeEnvironment;
    }
}
