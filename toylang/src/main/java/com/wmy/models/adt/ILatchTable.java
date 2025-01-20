package com.wmy.models.adt;

import java.util.Map;
import java.util.Set;

import com.wmy.exceptions.ExecutionException;

public interface ILatchTable extends ICloneable {
    Integer at(Integer key) throws ExecutionException;

    void set(Integer key, Integer value) throws ExecutionException;

    Integer add(Integer value);

    void del(Integer key);

    void setContent(Map<Integer, Integer> content);

    public boolean contains(Integer key);

    Set<Entry<Integer, Integer>> entrySet();

    ILatchTable deepCopy();
}
