package models.adt;

import java.util.ArrayList;

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
        return list.toString();
    }

    public int size() {
        return list.size();
    }

    public T get(int index) {
        return list.get(index);
    }
}