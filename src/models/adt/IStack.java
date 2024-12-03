package models.adt;

import java.util.stream.Stream;

public interface IStack<T> {
    void push(T elem);
    T pop();
    boolean isEmpty();
    String toString();
    Stream<T> stream();
}
