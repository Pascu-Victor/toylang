package com.wmy.models.adt;

import java.util.Map;
import java.util.Set;

import com.wmy.exceptions.ExecutionException;

public interface ILockTable extends ICloneable {
    Integer at(Integer key) throws ExecutionException;

    void set(Integer key, Integer value);

    Integer add(Integer value);

    void del(Integer key);

    void setContent(Map<Integer, Integer> content);

    boolean contains(Integer key);

    Set<Entry<Integer, Integer>> entrySet();
}
