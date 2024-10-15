package models.adt;

public interface IDict<TKey, TVal> {
    void set(TKey key, TVal value);
    TVal get(TKey key);
    boolean containsKey(TKey key);
    void remove(TKey key);
    String toString();
}