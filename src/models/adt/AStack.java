package models.adt;

import java.util.Stack;

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

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public String toString() {
        return stack.toString();
    }

}
