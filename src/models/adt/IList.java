package models.adt;

public interface IList<T> {
    void add(T elem);
    void remove(T elem);
    boolean contains(T elem);
    String toString();
}
