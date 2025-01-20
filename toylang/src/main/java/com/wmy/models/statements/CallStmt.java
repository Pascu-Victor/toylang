package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.ADict;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.adt.IList;
import com.wmy.models.expressions.IExp;
import com.wmy.models.types.IType;
import com.wmy.models.values.IValue;

public class CallStmt implements IStmt {
    CloneableString functionName;
    IList<IExp> arguments;

    public CallStmt(CloneableString functionName, IList<IExp> arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        for (IExp exp : arguments) {
            exp.typecheck(typeEnvironment);
        }
        return typeEnvironment;
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        if (!state.getProcTable().contains(functionName)) {
            throw new ExecutionException("Function " + functionName + " not found in ProcTable");
        }
        var procTableEntry = state.getProcTable().at(functionName);
        var params = procTableEntry.getParams();
        if (params.size() != arguments.size()) {
            throw new ExecutionException(
                    "Function "
                            + functionName
                            + " expected arg count: "
                            + params.size()
                            + " actual arg count: "
                            + +arguments.size()
                            + "\n arguments expected: "
                            + params.stream().map(e -> e.getKey() + " : " + e.getValue()).reduce("",
                                    (acc, e) -> acc + e + ", ")
                            + "\n arguments gotten: "
                            + arguments.stream().map(e -> e.toString()).reduce("", (acc, e) -> acc + e + ", "));
        }
        var newSymTable = new ADict<CloneableString, IValue>();
        for (int i = 0; i < params.size(); i++) {
            var argExpRes = arguments.get(i).eval(state.getSymTable(), state.getHeap());
            if (!argExpRes.getType().equals(params.get(i).getValue())) {
                throw new ExecutionException("Function " + functionName + " expected argument " + i + " to be of type "
                        + params.get(i).getValue() + ", got " + argExpRes.getType());
            }
            newSymTable.set(params.get(i).getKey(), argExpRes);
        }
        state.getExeStack().push(new ReturnStmt());
        state.getExeStack().push(procTableEntry.getProcedure());
        state.pushSymTable(newSymTable);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new CallStmt(functionName.deepCopy(), arguments.deepCopy());
    }

    @Override
    public String toString() {
        return "call " + functionName + "("
                + arguments.stream().map(e -> e.toString()).reduce("", (acc, e) -> acc + e + ", ")
                + ")";
    }
}
