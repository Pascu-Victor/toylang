package models.adt;

import models.values.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Set;

import exceptions.ExecutionException;


public class Heap implements IHeap {
    
    private Map<Integer, HeapEntry> vals;

    private int addr = 1;

    public IValue at(int addr) throws ExecutionException{
        try {
            return vals.get(addr).getVal();
        } catch (Exception e) {
            throw new ExecutionException("Invalid reference acess " + addr);
        }
    }

    public void set(int addr, IValue val) throws ExecutionException {
        try {
            vals.put(addr, new HeapEntry(val));
        } catch (Exception e) {
            throw new ExecutionException("Invalid reference acess " + addr);
        }
    }

    public int add(IValue val) {
        vals.put(addr++, new HeapEntry(val));
        return vals.size();
    }

    public void del(int addr) {
        vals.remove(addr);
    }

    public int addRef(int addr) {
        return vals.get(addr).incRefCount();
    }

    public int remRef(int addr) {
        return vals.get(addr).decRefCount();
    }

    public int refcount(int addr) {
        return vals.get(addr).getRefCount();
    }

    public boolean contains(int addr) {
        return vals.containsKey(addr);
    }

    public Set<Entry<Integer, HeapEntry>> entrySet() {
        return vals.entrySet();
    }

    public void setContent(Map<Integer, HeapEntry> content) {
        this.vals = content;
    }

    public Heap() {
        vals = new HashMap<>();
    }

    public Collection<Integer> getValues() {
        return vals.keySet();
    }

    @Override
    public String toString() {
        return vals.entrySet().stream()
            .map(e -> e.getKey() + "->" + e.getValue())
            .collect(Collectors.joining("\n"));
    }
}
