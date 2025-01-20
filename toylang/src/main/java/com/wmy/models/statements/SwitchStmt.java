package com.wmy.models.statements;

import com.wmy.models.adt.Entry;
import com.wmy.exceptions.ExecutionException;
import com.wmy.exceptions.TypeException;
import com.wmy.models.PrgState;
import com.wmy.models.adt.AList;
import com.wmy.models.adt.CloneableString;
import com.wmy.models.adt.IDict;
import com.wmy.models.adt.IList;
import com.wmy.models.expressions.IExp;
import com.wmy.models.types.IType;

public class SwitchStmt implements IStmt {
    private IExp swExp;
    private IList<Entry<IExp, IStmt>> cases;
    private IStmt defaultCase;

    public SwitchStmt(IExp swExp, IList<Entry<IExp, IStmt>> cases, IStmt defaultCase) {
        this.swExp = swExp;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }

    @Override
    public IDict<CloneableString, IType> typecheck(IDict<CloneableString, IType> typeEnvironment) throws TypeException {
        swExp.typecheck(typeEnvironment);
        for (var c : this.cases.stream().toList()) {
            c.getKey().typecheck(typeEnvironment);
            c.getValue().typecheck(typeEnvironment);
        }
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "Switch(" + this.swExp + "):" +
                cases.stream().map(c -> "case(" + c.getKey() + ") {\n" + c.getValue() + "\n}").reduce("",
                        (r, c) -> r + "\n" + c)
                +
                "default {\n" + defaultCase + "\n}";
    }

    @Override
    public PrgState execute(PrgState state) throws ExecutionException {
        var expRes = swExp.eval(state.getSymTable(), state.getHeap());
        for (var c : cases) {
            var caseExpRes = c.getKey().eval(state.getSymTable(), state.getHeap());
            if (expRes.equals(caseExpRes)) {
                return c.getValue().execute(state);
            }
        }
        if (defaultCase == null) {
            return null;
        }
        return defaultCase.execute(state);
    }

    @Override
    public IStmt deepCopy() {
        var newCases = new AList<Entry<IExp, IStmt>>();
        this.cases.forEach(c -> newCases.add(c.deepCopy()));
        IStmt defaultCase = null;
        if (this.defaultCase != null) {
            defaultCase = this.defaultCase.deepCopy();
        }
        return new SwitchStmt(swExp.deepCopy(), newCases, defaultCase);
    }
}
