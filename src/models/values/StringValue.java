package models.values;

import models.adt.CloneableString;
import models.types.IType;
import models.types.StringType;

public class StringValue implements IValue {
    CloneableString val;

    public StringValue(CloneableString val) {
        this.val = val;
    }

    public StringValue(String val) {
        this.val = new CloneableString(val);
    }

    public CloneableString getVal() {
        return val;
    }

    public IType getType() {
        return new StringType();
    }

    public IValue deepCopy() {
        return new StringValue(val.deepCopy());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StringValue) {
            return ((StringValue) obj).getVal() == val;
        }
        return false;
    }

    @Override
    public String toString() {
        return "\"" + val + "\"";
    }
    
}
