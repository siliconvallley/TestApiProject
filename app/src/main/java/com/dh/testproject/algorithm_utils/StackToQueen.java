package com.dh.testproject.algorithm_utils;

import java.util.Stack;

public class StackToQueen<T> {
    private static final String TAG = "StackToQueen";
    private Stack<T> inputStack = new Stack<>();
    private Stack<T> outPutStack = new Stack<>();

    public void push(T t) {
        inputStack.push(t);
    }

    public T peek() {
        if (outPutStack.empty()) {
            if (inputStack.empty()) {
                return null;
            }
            while (!inputStack.empty()) {
                outPutStack.push(inputStack.pop());
            }
            return outPutStack.peek();
        } else {
            return outPutStack.peek();
        }
    }

    public T pop() {
        if (outPutStack.empty()) {
            if (inputStack.empty()) {
                return null;
            }
            while (!inputStack.empty()) {
                outPutStack.push(inputStack.pop());
            }
            return outPutStack.pop();
        } else {
            return outPutStack.pop();
        }
    }

    public boolean empty() {
        return outPutStack.empty() && inputStack.empty();
    }
}
