package models.values;

import models.types.IType;
import models.types.StringType;

public class StringValue implements IValue {
    String val;

    public StringValue(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public IType getType() {
        return new StringType();
    }

    public IValue deepCopy() {
        return new StringValue(val);
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
