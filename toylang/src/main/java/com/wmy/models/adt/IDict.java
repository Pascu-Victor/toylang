package com.wmy.models.adt;

import java.util.Collection;
import java.util.Map.Entry;

public interface IDict<TKey, TVal> {
    void set(TKey key, TVal value);

    TVal get(TKey key);

    boolean containsKey(TKey key);

    void remove(TKey key);

    String toString();

    IDict<TKey, TVal> deepCopy();

    IList<TKey> keys();

    Collection<TVal> values();

    Collection<Entry<TKey, TVal>> entrySet();
}