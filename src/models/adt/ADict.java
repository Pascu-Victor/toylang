package models.adt;

import java.util.Map;

public class ADict<TKey, TVal> implements IDict<TKey, TVal> {
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

    public ADict() {
        dict = new java.util.HashMap<TKey, TVal>();
    }

    public String toString() {
        return dict.toString();
    }
}
