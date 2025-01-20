package com.wmy.models.adt;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ADict<TKey extends ICloneable, TVal extends ICloneable> implements IDict<TKey, TVal> {
    private Map<TKey, TVal> dict;

    public void set(TKey key, TVal value) {
        dict.put(key, value);
    }

    public TVal get(TKey key) {
        return dict.get(key);
    }

    public boolean containsKey(TKey key) {
        return dict.containsKey(key);
    }

    public void remove(TKey key) {
        dict.remove(key);
    }

    public Collection<TVal> values() {
        return dict.values();
    }

    public ADict() {
        dict = new java.util.HashMap<TKey, TVal>();
    }

    public ADict(Map<TKey, TVal> data) {
        this.dict = data;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IDict<TKey, TVal> deepCopy() {
        var hm = new HashMap<TKey, TVal>();
        for (var i : dict.entrySet()) {
            hm.put((TKey) i.getKey().deepCopy(), (TVal) i.getValue().deepCopy());
        }
        return new ADict<>(hm);
    }

    public String toString() {
        return dict.entrySet().stream()
                .map(e -> e.getKey() + "-->" + e.getValue())
                .collect(Collectors.joining("\n"));
    }

    public IList<TKey> keys() {
        var keys = new AList<TKey>();
        for (Map.Entry<TKey, TVal> entry : dict.entrySet()) {
            keys.add(entry.getKey());
        }
        return keys;
    }

    public Collection<Entry<TKey, TVal>> entrySet() {
        return dict.entrySet().stream().map(e -> new Entry<>(e.getKey(), e.getValue())).collect(Collectors.toSet());
    }

}
