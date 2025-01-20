package com.wmy.models.adt;

import java.util.Map;
import java.util.Set;

import com.wmy.exceptions.ExecutionException;

public class LatchTable implements ILatchTable {
    private Map<Integer, Integer> vals;
    private int currentKey = 1;

    @Override
    synchronized public Integer add(Integer value) {
        vals.put(currentKey++, value);
        return vals.size();
    }

    @Override
    synchronized public void del(Integer key) {
        vals.remove(key);
    }

    @Override
    public Integer at(Integer key) throws ExecutionException {
        try {
            return vals.get(key);
        } catch (Exception e) {
            throw new ExecutionException("Invalid reference acess " + key);
        }
    }

    @Override
    public boolean contains(Integer key) {
        return vals.containsKey(key);
    }

    @Override
    public Set<Entry<Integer, Integer>> entrySet() {
        return vals.entrySet().stream()
                .map(e -> new Entry<Integer, Integer>(e.getKey(), e.getValue()))
                .collect(java.util.stream.Collectors.toSet());
    }

    @Override
    synchronized public void set(Integer key, Integer value) throws ExecutionException {
        try {
            vals.put(key, value);
        } catch (Exception e) {
            throw new ExecutionException("Invalid reference acess " + key);
        }
    }

    @Override
    synchronized public void setContent(Map<Integer, Integer> content) {
        vals = content;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LatchTable) {
            var other = (LatchTable) obj;
            return vals.equals(other.vals);
        }
        return false;
    }

    @Override
    public String toString() {
        return vals.entrySet().stream()
                .map(e -> e.getKey() + "-->" + e.getValue())
                .reduce("", (acc, e) -> acc + e + "\n");
    }

    public LatchTable() {
        vals = new java.util.HashMap<Integer, Integer>();
    }

    public LatchTable(Map<Integer, Integer> data) {
        this.vals = data;
    }

    @Override
    public ILatchTable deepCopy() {
        var newVals = new java.util.HashMap<Integer, Integer>();
        vals.forEach((k, v) -> newVals.put(k, v));
        return new LatchTable(newVals);
    }
}
