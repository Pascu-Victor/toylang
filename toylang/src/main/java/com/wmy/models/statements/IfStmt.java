package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.expressions.IExp;
import com.wmy.models.types.BoolType;
import com.wmy.models.types.IType;
import com.wmy.models.values.BoolValue;

public class IfStmt implements IStmt {
    private IStmt thenS;
    private IStmt elseS;
    private IExp exp;

    public IfStmt(IExp exp, IStmt thenS, IStmt elseS) {
        this.exp = exp;
        this.thenS = thenS;
        this.elseS = elseS;
    }

    public IExp getExp() {
        return exp;
    }

    public String toString() {
        return "if (" + exp.toString() + ") then (" + thenS.toString() + ") else (" + elseS.toString() + ")";
    }

    public PrgState execute(PrgState state) throws ExecutionException {
        var r = exp.eval(state.getSymTable(), state.getHeap());
        if (r instanceof BoolValue) {
            if (((BoolValue)r).getVal()) {
                state.getExeStack().push(thenS);
            } else {
                state.getExeStack().push(elseS);
            }
            return null;
        }
        if(r == null)
            throw new ExecutionException("Invalid IfStmt: Expression evaluation returned null");
        throw new ExecutionException("Invalid IfStmt: Cannot coerce expression (Type: "+ r.getType().toString() +") to BoolValue");
    }

    public IfStmt deepCopy() {
        return new IfStmt(exp.deepCopy(), thenS.deepCopy(), elseS.deepCopy());
    }
    
    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        IType expType = exp.typecheck(typeEnvironment);
        if (!expType.equals(new BoolType())) {
            throw new TypeException("IfStmt: Expression type is not BoolType, got: " + expType);
        }
        thenS.typecheck(typeEnvironment.deepCopy());
        elseS.typecheck(typeEnvironment.deepCopy());
        return typeEnvironment;
    }
}
