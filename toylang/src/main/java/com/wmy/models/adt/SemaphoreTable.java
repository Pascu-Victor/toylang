package com.wmy.models.adt;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.wmy.exceptions.ExecutionException;

public class SemaphoreTable implements ISemaphoreTable {
    private Map<Integer, Entry<Integer, IList<Integer>>> semaphoreTable;
    private static int nextId = 0;

    public SemaphoreTable() {
        semaphoreTable = new HashMap<>();
    }

    @Override
    public synchronized Entry<Integer, IList<Integer>> at(Integer key) throws ExecutionException {
        if (!semaphoreTable.containsKey(key)) {
            throw new ExecutionException("SemaphoreTable: key not found");
        }
        return semaphoreTable.get(key);
    }

    @Override
    public synchronized Integer add(Integer key, IList<Integer> value) {
        semaphoreTable.put(nextId, new Entry<>(key, value));
        return nextId++;
    }

    @Override
    public synchronized void set(Integer key, Entry<Integer, IList<Integer>> value) {
        semaphoreTable.put(key, value);
    }

    @Override
    public synchronized void del(Integer key) {
        semaphoreTable.remove(key);
    }

    @Override
    public synchronized void setContent(Map<Integer, Entry<Integer, IList<Integer>>> content) {
        semaphoreTable = content;
    }

    @Override
    public synchronized boolean contains(Integer key) {
        return semaphoreTable.containsKey(key);
    }

    @Override
    public synchronized Set<Entry<Integer, Entry<Integer, IList<Integer>>>> entrySet() {
        return semaphoreTable.entrySet().stream()
                .map(e -> new Entry<>(e.getKey(), e.getValue()))
                .collect(java.util.stream.Collectors.toSet());
    }

    @Override
    public String toString() {
        return semaphoreTable.entrySet().stream()
                .map(e -> e.getKey() + " -> " + "(" + e.getValue().getKey() + ":["
                        + e.getValue().getValue().stream().map(i -> i.toString()).collect(Collectors.joining(","))
                        + "])")
                .reduce("", (acc, e) -> acc + e + "\n");
    }

}
