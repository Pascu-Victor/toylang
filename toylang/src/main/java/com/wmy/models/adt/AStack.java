package com.wmy.models.adt;

import java.util.Stack;
import java.util.stream.Stream;

public class AStack<T> implements IStack<T> {
    private Stack<T> stack;

    public AStack() {
        stack = new Stack<T>();
    }

    public void push(T elem) {
        stack.push(elem);
    }

    public T pop() {
        return stack.pop();
    }

    public T top() {
        return stack.peek();
    }

    public IStack<T> deepCopy() {
        var copy = new AStack<T>();
        stack.forEach(copy::push);
        return copy;
    }

    public Stream<T> stream() {
        return stack.stream();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }
}
