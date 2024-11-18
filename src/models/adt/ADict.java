package models.adt;

import java.util.Collection;
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

    public Collection<TVal> values() {
        return dict.values();
    }

    public ADict() {
        dict = new java.util.HashMap<TKey, TVal>();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<TKey, TVal> entry : dict.entrySet()) {
            sb.append(entry.getKey()).append(" --> ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    public AList<TKey> keys() {
        var keys = new AList<TKey>();
        for (Map.Entry<TKey,TVal> entry : dict.entrySet()) {
            keys.add(entry.getKey());
        }
        return keys;
    }
}
