package com.wmy.models.adt;

import java.util.stream.Stream;

public interface IStack<T> {
    void push(T elem);

    T pop();

    T top();

    boolean isEmpty();

    String toString();

    Stream<T> stream();

    IStack<T> deepCopy();
}
