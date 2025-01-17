package com.wmy.models.adt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AList<T> implements IList<T> {
    private ArrayList<T> list;

    public AList() {
        list = new ArrayList<T>();
    }

    public void add(T elem) {
        list.add(elem);
    }

    public void remove(T elem) {
        list.remove(elem);
    }

    public boolean contains(T elem) {
        return list.contains(elem);
    }

    public String toString() {
        return list.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n")) + "\n";
    }

    public int size() {
        return list.size();
    }

    public T get(int index) {
        return list.get(index);
    }

    public Stream<T> stream() {
        return list.stream();
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return list.spliterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        list.forEach(action);
    }

    @Override
    public boolean equals(Object obj) {
        return list.equals(obj);
    }
}