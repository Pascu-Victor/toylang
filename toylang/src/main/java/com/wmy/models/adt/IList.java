package com.wmy.models.adt;

import java.util.stream.Stream;

public interface IList<T extends ICloneable> extends Iterable<T>, ICloneable {
    void add(T elem);

    void remove(T elem);

    boolean contains(T elem);

    int size();

    T get(int index);

    String toString();

    Stream<T> stream();

    IList<T> deepCopy();
}
