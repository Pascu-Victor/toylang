package com.wmy.models.values;

import com.wmy.models.types.IType;
import com.wmy.models.types.RefType;

public class RefValue implements IValue {
    private int addr;
    IType locationType;

    public RefValue(int addr, IType locationType) {
        this.addr = addr;
        this.locationType = locationType;
    }

    public int getAddr() {
        return this.addr;
    }

    public IType getType() {
        return new RefType(locationType);
    }

    public IValue deepCopy() {
        return new RefValue(this.addr, this.locationType);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RefValue) {
            return 
                ((RefValue) obj).getAddr() == this.addr
            &&  ((RefValue) obj).getType() == this.locationType; 
        }
        return false;
    }

    @Override
    public String toString() {
        return "Ref(" + Integer.toString(addr) + "," + this.locationType + ")";
    }
}
