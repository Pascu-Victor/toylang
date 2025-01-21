package com.wmy.models.adt;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.wmy.exceptions.ExecutionException;

public class ProcTable implements IProcTable {
    private Map<CloneableString, ProcTableEntry> content;

    public ProcTable() {
        content = new HashMap<>();
    }

    public ProcTable(Map<CloneableString, ProcTableEntry> content) {
        this.content = content;
    }

    @Override
    public ProcTableEntry at(CloneableString key) throws ExecutionException {
        if (!content.containsKey(key)) {
            throw new ExecutionException("Key not found in ProcTable");
        }
        return content.get(key);
    }

    @Override
    public void set(CloneableString key, ProcTableEntry value) {
        content.put(key, value);
    }

    @Override
    public void del(CloneableString key) {
        content.remove(key);
    }

    @Override
    public void setContent(Map<CloneableString, ProcTableEntry> content) {
        this.content = content;
    }

    @Override
    public boolean contains(CloneableString key) {
        return content.containsKey(key);
    }

    @Override
    public Set<Entry<CloneableString, ProcTableEntry>> entrySet() {
        return content.entrySet().stream().map(e -> new Entry<>(e.getKey(), e.getValue())).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return content.entrySet().stream().map(e -> "procedure " + e.getKey() + " " + e.getValue())
                .collect(Collectors.joining("\n"));
    }

    @Override
    public Object deepCopy() {
        Map<CloneableString, ProcTableEntry> newContent = new HashMap<>();
        content.forEach((k, v) -> newContent.put(k.deepCopy(), v.deepCopy()));
        return new ProcTable(newContent);
    }
}
