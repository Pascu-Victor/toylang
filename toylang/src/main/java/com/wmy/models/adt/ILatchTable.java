package com.wmy.models.adt;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.wmy.exceptions.ExecutionException;

public interface ILatchTable {
    Integer at(Integer key) throws ExecutionException;

    void set(Integer key, Integer value) throws ExecutionException;

    Integer add(Integer value);

    void del(Integer key);

    void setContent(Map<Integer, Integer> content);

    public boolean contains(Integer key);

    Set<Entry<Integer, Integer>> entrySet();
}
