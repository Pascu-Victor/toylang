package com.wmy.models.adt;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.wmy.exceptions.ExecutionException;

public class BarrierTable implements IBarrierTable {
    private Map<Integer, BarrierTableEntry> content;

    private static int barrierId = 0;

    public BarrierTable() {
        this.content = new HashMap<>();
    }

    public BarrierTable(Map<Integer, BarrierTableEntry> content) {
        this.content = content;
    }

    @Override
    public synchronized BarrierTableEntry at(Integer key) throws ExecutionException {
        if (!content.containsKey(key)) {
            throw new ExecutionException("Key " + key + " not found in BarrierTable");
        }
        return content.get(key);
    }

    @Override
    public synchronized void set(Integer key, BarrierTableEntry value) {
        content.put(key, value);
    }

    @Override
    public synchronized Integer add(Integer count, IList<Integer> waitList) {
        content.put(barrierId, new BarrierTableEntry(count, waitList));
        return barrierId++;
    }

    @Override
    public synchronized void del(Integer key) {
        content.remove(key);
    }

    @Override
    public synchronized void setContent(Map<Integer, BarrierTableEntry> content) {
        this.content = content;
    }

    @Override
    public synchronized boolean contains(Integer key) {
        return content.containsKey(key);
    }

    @Override
    public synchronized Set<Entry<Integer, BarrierTableEntry>> entrySet() {
        return content.entrySet().stream().map(e -> new Entry<>(e.getKey(), e.getValue())).collect(Collectors.toSet());
    }

    @Override
    public synchronized String toString() {
        return content.entrySet().stream().map(e -> e.getKey() + " " + e.getValue()).collect(Collectors.joining("\n"));
    }

    @Override
    public synchronized Object deepCopy() {
        Map<Integer, BarrierTableEntry> newContent = new HashMap<>();
        content.forEach((k, v) -> newContent.put(k, (BarrierTableEntry) v.deepCopy()));
        return new BarrierTable(newContent);
    }
}
