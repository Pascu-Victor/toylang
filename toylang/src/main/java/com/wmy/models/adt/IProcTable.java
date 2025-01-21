package com.wmy.models.adt;

import java.util.Map;
import java.util.Set;

import com.wmy.exceptions.ExecutionException;
import com.wmy.models.statements.IStmt;
import com.wmy.models.types.IType;

public interface IProcTable extends ICloneable {
    public class ProcTableEntry implements ICloneable {
        private IList<Entry<CloneableString, IType>> params;
        private IStmt procedure;

        public ProcTableEntry(IList<Entry<CloneableString, IType>> params, IStmt procedure) {
            this.params = params;
            this.procedure = procedure;
        }

        public IList<Entry<CloneableString, IType>> getParams() {
            return params;
        }

        public IStmt getProcedure() {
            return procedure;
        }

        @Override
        public ProcTableEntry deepCopy() {
            return new ProcTableEntry(params.deepCopy(), procedure.deepCopy());
        }

        @Override
        public String toString() {
            return "(" + params + ") {" + procedure + "}";
        }
    }

    ProcTableEntry at(CloneableString key) throws ExecutionException;

    void set(CloneableString key, ProcTableEntry value);

    void del(CloneableString key);

    void setContent(Map<CloneableString, ProcTableEntry> content);

    public boolean contains(CloneableString key);

    Set<Entry<CloneableString, ProcTableEntry>> entrySet();
}
