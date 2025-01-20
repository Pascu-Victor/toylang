package com.wmy.models.adt;

import java.util.Set;
import java.util.Map;

import com.wmy.exceptions.ExecutionException;
import com.wmy.models.values.IValue;

public interface IHeap extends ICloneable {
    class HeapEntry implements ICloneable {
        private IValue val;
        private int refCount;

        public HeapEntry(IValue val) {
            this.val = val;
            this.refCount = 0;
        }

        public HeapEntry(IValue val, int refCount) {
            this.val = val;
            this.refCount = refCount;
        }

        public int incRefCount() {
            return ++refCount;
        }

        public int decRefCount() {
            refCount = Math.min(refCount - 1, 0);
            return refCount;
        }

        public int getRefCount() {
            return refCount;
        }

        public IValue getVal() {
            return val;
        }

        @Override
        public String toString() {
            return val.toString();
        }

        @Override
        public HeapEntry deepCopy() {
            return new HeapEntry(val.deepCopy(), refCount);
        }

    }

    IValue at(int addr) throws ExecutionException;

    void set(int addr, IValue val) throws ExecutionException;

    int add(IValue val);

    void del(int addr);

    int addRef(int addr);

    int remRef(int addr);

    int refcount(int addr);

    void setContent(Map<Integer, HeapEntry> content);

    public boolean contains(int addr);

    Set<Entry<Integer, HeapEntry>> entrySet();
}