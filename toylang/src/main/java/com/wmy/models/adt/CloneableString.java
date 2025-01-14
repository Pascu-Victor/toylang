package com.wmy.models.adt;

public class CloneableString implements ICloneable {
    private final String data;

    public CloneableString(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data;
    }

    public CloneableString deepCopy() {
        return new CloneableString(this.data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }
    
    public String innerValue() {
        return data;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CloneableString) {
            var a = data.trim();
            var b = ((CloneableString)obj).innerValue().trim();
            return a.equals(b);
        }
        if (obj instanceof String) {
            return data == (String)obj;
        }
        return false;
    }

    @Override
    protected Object clone() {
        return new CloneableString(data);
    }
}
