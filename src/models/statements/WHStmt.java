package models.statements;

import exceptions.ExecutionException;
import exceptions.TypeException;
import models.PrgState;
import models.adt.CloneableString;
import models.adt.IDict;
import models.expressions.IExp;
import models.types.IType;
import models.types.RefType;
import models.values.IValue;
import models.values.RefValue;

public class WHStmt implements IStmt {
    private CloneableString varName;
    private IExp exp;

    public WHStmt(CloneableString varName, IExp exp) {
        this.varName = varName;
        this.exp = exp;
    }

    public WHStmt(String varName, IExp exp) {
        this.varName = new CloneableString(varName);
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "wH(" + varName + ", " + exp.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        if (!state.getSymTable().containsKey(varName)) {
            throw new ExecutionException("Variable " + varName + " is not defined");
        }

        if (!(state.getSymTable().get(varName).getType() instanceof RefType)) {
            throw new ExecutionException("Variable " + varName + " is not a reference");
        }

        
        RefType refType = (RefType)(state.getSymTable().get(varName).getType());
        RefValue refVal = ((RefValue)state.getSymTable().get(varName));
        if (!(state.getHeap().contains(refVal.getAddr()))) {
            throw new ExecutionException("Reference to undefined heap address" + refVal);
        }

        if(!(state.getHeap().at(refVal.getAddr()).getType().equals(refType.getInner()))) {
            throw new ExecutionException(
                "Reference " +
                refVal +
                " references object in heap of type " +
                state.getHeap().at(refVal.getAddr()).getType() +
                " expected type " +
                refType
            );
        }
        
        IValue valueToSet = exp.eval(state.getSymTable(), state.getHeap());

        if (!(valueToSet.getType().equals(refType.getInner()))) {
            throw new ExecutionException("Expression returned type: " + valueToSet.getType() + " but needed " + refType.getInner());
        }

        state.getHeap().set(refVal.getAddr(), valueToSet);

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new WHStmt(varName, exp.deepCopy());
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        var expType = exp.typecheck(typeEnvironment);
        var varType = typeEnvironment.get(varName);

        if(!(varType instanceof RefType)) {
            throw new TypeException("var: " + varName + " not instance of type RefType, got: " + varType);
        }
        
        if(!varType.equals(new RefType(expType))) {
            throw new TypeException("var: " + varName + " does not match expression type: " +expType);
        }

        return typeEnvironment;
    }
}
