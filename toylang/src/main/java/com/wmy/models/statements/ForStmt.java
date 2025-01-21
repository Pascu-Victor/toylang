package com.wmy.models.statements;

import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.expressions.IExp;
import com.wmy.models.types.BoolType;
import com.wmy.models.types.IType;

public class ForStmt implements IStmt {
    private CloneableString varName;
    private IType varType;
    private IExp startExp;
    private IExp endExp;
    private IStmt body;
    private IExp stepExp;
    private CloneableString stepVar;

    public ForStmt(CloneableString varName, IType varType, IExp startExp, IExp endExp, CloneableString stepVar,
            IExp stepExp, IStmt body) {
        this.varName = varName;
        this.varType = varType;
        this.startExp = startExp;
        this.stepVar = stepVar;
        this.endExp = endExp;
        this.body = body;
        this.stepExp = stepExp;
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        if (!varType.equals(startExp.typecheck(typeEnvironment))) {
            throw new TypeException("Start expression type does not match variable type");
        }
        var env = typeEnvironment.deepCopy();
        env.set(varName, varType);
        if (!endExp.typecheck(env).equals(new BoolType())) {
            throw new TypeException("End expression type is not boolean");
        }
        var stepVarType = env.get(stepVar);
        if (stepVarType == null) {
            throw new TypeException("Step variable not declared");
        }
        if (!stepVarType.equals(stepExp.typecheck(env))) {
            throw new TypeException("Step expression type does not match variable type");
        }
        body.typecheck(env);
        return typeEnvironment;
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        var stmt = new CompStmt(
                new VarDeclStmt(this.varName, this.varType),
                new CompStmt(
                        new AssignStmt(varName, startExp),
                        new WhileStmt(endExp, new CompStmt(body,
                                new AssignStmt(stepVar, stepExp)))));
        state.getExeStack().push(stmt);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new ForStmt(varName, varType, startExp.deepCopy(), endExp.deepCopy(), stepVar.deepCopy(),
                stepExp.deepCopy(), body.deepCopy());
    }

    @Override
    public String toString() {
        return "for (" + varName + " = " + startExp + "; " + endExp + "; " + stepVar + " = " + stepExp + ") {" + body
                + "}";
    }
}
