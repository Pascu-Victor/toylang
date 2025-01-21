package com.wmy.models.adt;

import java.util.Map;
import java.util.Set;

import com.wmy.exceptions.ExecutionException;

public interface ISemaphoreTable {
    Entry<Integer, IList<Integer>> at(Integer key) throws ExecutionException;

    void set(Integer key, Entry<Integer, IList<Integer>> value);

    Integer add(Integer key, IList<Integer> value);

    void del(Integer key);

    void setContent(Map<Integer, Entry<Integer, IList<Integer>>> content);

    public boolean contains(Integer key);

    Set<Entry<Integer, Entry<Integer, IList<Integer>>>> entrySet();
}
