package com.wmy.models.adt;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.wmy.exceptions.ExecutionException;

public class LockTable implements ILockTable {
    private Map<Integer, Integer> content;
    private static int nextId = 0;

    public LockTable() {
        this.content = new HashMap<>();
    }

    public LockTable(Map<Integer, Integer> other) {
        this.content = other;
    }

    @Override
    public synchronized Integer at(Integer key) throws ExecutionException {
        if (!content.containsKey(key)) {
            throw new ExecutionException("Key not found in LockTable");
        }
        return content.get(key);
    }

    @Override
    public synchronized void set(Integer key, Integer value) {
        content.put(key, value);
    }

    @Override
    public synchronized Integer add(Integer value) {
        content.put(nextId, value);
        return nextId++;
    }

    @Override
    public synchronized void del(Integer key) {
        content.remove(key);
    }

    @Override
    public synchronized void setContent(Map<Integer, Integer> content) {
        this.content = content;
    }

    @Override
    public synchronized boolean contains(Integer key) {
        return content.containsKey(key);
    }

    @Override
    public synchronized Set<Entry<Integer, Integer>> entrySet() {
        return content.entrySet().stream().map(e -> new Entry<>(e.getKey(), e.getValue())).collect(Collectors.toSet());
    }

    @Override
    public synchronized LockTable deepCopy() {
        var newContent = new HashMap<Integer, Integer>();
        content.forEach((k, v) -> newContent.put(k, v));
        return new LockTable(newContent);
    }

    @Override
    public synchronized String toString() {
        return content.entrySet().stream().map(e -> e.getKey() + "->" + e.getValue()).collect(Collectors.joining("\n"));
    }
}
