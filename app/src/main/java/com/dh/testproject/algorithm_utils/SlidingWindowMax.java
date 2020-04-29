package com.dh.testproject.algorithm_utils;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

public class SlidingWindowMax {

    public Queue<Integer> maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return null;
        }

        ArrayDeque<Integer> windowQueue = new ArrayDeque<>();
        Queue<Integer> resQueue = new LinkedList<>();

        for (int i = 0; i < nums.length; i++) {
            if (i >= k && windowQueue.peekFirst() <= i - k) {
                windowQueue.pollFirst();
            }
            while (!windowQueue.isEmpty() && nums[windowQueue.peekLast()] <= nums[i]) {
                windowQueue.pollLast();
            }
            windowQueue.offer(i);
            if (i >= k-1) {
                resQueue.offer(nums[windowQueue.peekFirst()]);
            }
        }
        return resQueue;
    }


    public int[] maxSlidingWindow1(int[] nums, int k) {
        int len = nums.length;
        if (len == 0 || k <= 0) {
            return new int[0];
        }
        int[] output = new int[len - k + 1];
        for (int i = 0; i < len - k + 1; i++) {
            int max = 0;
            for (int j = i; j < i + k; j++) {
                max = Math.max(max, nums[j]);
            }
            output[i] = max;
        }
        return output;
    }


}
