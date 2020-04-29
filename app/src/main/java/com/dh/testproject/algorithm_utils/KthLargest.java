package com.dh.testproject.algorithm_utils;

import java.util.PriorityQueue;

public class KthLargest {
    private PriorityQueue<Integer> priorityQueue;
    private int k;

    public PriorityQueue<Integer> getKthLargest(int k, int[] array) {
        priorityQueue = new PriorityQueue<>(k);
        this.k = k;
        for (int i : array) {
            add(i);
        }
        return priorityQueue;
    }

    private void add(int num) {
        if (priorityQueue.size() < k) {
            priorityQueue.offer(num);
        } else if (priorityQueue.peek() < num) {
            priorityQueue.poll();
            priorityQueue.offer(num);
        }
    }
}
