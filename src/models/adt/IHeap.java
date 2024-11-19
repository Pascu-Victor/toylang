package models.adt;

import java.util.Set;
import java.util.Map.Entry;

import exceptions.ExecutionException;
import models.values.IValue;

public interface IHeap {
    class HeapEntry {
        private IValue val;
        private int refCount;

        public HeapEntry(IValue val) {
            this.val = val;
            this.refCount = 0;
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

    }
    IValue at(int addr) throws ExecutionException;
    void set(int addr, IValue val) throws ExecutionException;
    int add(IValue val);
    void del(int addr);
    int addRef(int addr);
    int remRef(int addr);
    int refcount(int addr);
    public boolean contains(int addr);
    Set<Entry<Integer, HeapEntry>> entrySet();
}