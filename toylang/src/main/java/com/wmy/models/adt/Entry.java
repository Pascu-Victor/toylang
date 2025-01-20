package com.wmy.models.adt;

public class Entry<K, V> implements java.util.Map.Entry<K, V>, ICloneable {
    private final K key;
    private V value;

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Entry<K, V> deepCopy() {
        K newK;
        V newV;
        if (key instanceof ICloneable) {
            newK = (K) ((ICloneable) key).deepCopy();
        } else {
            newK = key;
        }
        if (value instanceof ICloneable) {
            newV = (V) ((ICloneable) value).deepCopy();
        } else {
            newV = value;
        }
        return new Entry<K, V>(newK, newV);
    }

    Entry<K, V> asEntry() {
        return this;
    }

}
