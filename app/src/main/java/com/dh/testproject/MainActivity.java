package com.dh.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dh.testproject.activity.Main2Activity;
import com.dh.testproject.activity.Main3Activity;
import com.dh.testproject.activity.Main4Activity;
import com.dh.testproject.activity.Main5Activity;
import com.dh.testproject.activity.Main6Activity;
import com.dh.testproject.algorithm_utils.SlidingWindowMax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Button btJudge;
    private Button btContentProvider;
    private Button btEncrypt;
    private Button btDataBind;
    private Button btNotification;
    private Button btPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initListener();
    }

    private void initViews() {
        btJudge = findViewById(R.id.btJudge);
        btContentProvider = findViewById(R.id.btContentProvider);
        btEncrypt = findViewById(R.id.btEncrypt);
        btDataBind = findViewById(R.id.btDataBind);
        btNotification = findViewById(R.id.btNotification);
        btPermission = findViewById(R.id.btPermission);
    }

    private void initListener() {
        btJudge.setOnClickListener(this);
        btContentProvider.setOnClickListener(this);
        btEncrypt.setOnClickListener(this);
        btDataBind.setOnClickListener(this);
        btNotification.setOnClickListener(this);
        btPermission.setOnClickListener(this);
    }

    public void btJudge(View view) {
        /*String str = "(){}[]()(([])){[]}";
        if (isValid(str)) {
            Log.e(TAG, "合法");
        } else {
            Log.e(TAG, "非法");
        }*/


        /*StackToQueen<Integer> queen = new StackToQueen<>();
        queen.push(1);
        queen.push(2);
        queen.push(3);

        queen.pop();
        Log.e(TAG,"队顶元素: " + queen.peek());
        queen.pop();
        Log.e(TAG,"队顶元素: " + queen.peek());
        queen.pop();

        queen.push(4);
        queen.push(5);
        queen.push(6);
        Log.e(TAG,"队顶元素: " + queen.peek());*/

        /*int[] a = new int[]{50, 10, 20, 15, 95, 30, 78, 65, 45, 85, 45, 63};
        KthLargest kthLargest = new KthLargest();
        PriorityQueue<Integer> queue = kthLargest.getKthLargest(3, a);
        while (!queue.isEmpty()) {
            Log.e(TAG, "最大的K个数： " + queue.poll());
        }*/

        //int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        int[] nums = {1, 3, 1, 2, 0, 5};
        SlidingWindowMax slidingWindowMax = new SlidingWindowMax();
        int[] slidingWindow1 = slidingWindowMax.maxSlidingWindow1(nums, 3);
        for (int i : slidingWindow1) {
            Log.e(TAG, "滑动窗口每次滑动的最大数： " + i);
        }
        Log.e(TAG, "===========================================");
        Queue<Integer> integers = slidingWindowMax.maxSlidingWindow(nums, 3);
        while (!integers.isEmpty()) {
            Log.e(TAG, "滑动窗口每次滑动的最大数： " + integers.poll());
        }

        //new StatFs("/data").getBlockSizeLong();
    }

    /**
     * =========================判断括号合法性===========================
     */
    private boolean isValid(String s) {
        int len;
        do {
            len = s.length();
            s = s.replace("()", "").replace("{}", "").replace("[]", "");
        } while (len != s.length());
        return s.length() == 0;
    }

    private boolean isInvalid(String brackets) {
        char[] chars = brackets.toCharArray();
        if (chars.length == 0) return true;
        Map<Character, Character> map = new HashMap<>();
        map.put(')', '(');
        map.put(']', '[');
        map.put('}', '{');

        Stack<Character> stack = new Stack<>();
        for (char aChar : chars) {
            if (!isContains(aChar)) {
                return false;
            }
            if (aChar == '(' || aChar == '[' || aChar == '{') {
                stack.push(aChar);
            } else if (!stack.empty() && map.get(aChar) == stack.peek()) {
                stack.pop();
            } else {
                return false;
            }
        }
        return stack.empty();
    }

    private boolean isContains(char c) {
        List<Character> list = new ArrayList<>();
        list.add('(');
        list.add(')');
        list.add('[');
        list.add(']');
        list.add('{');
        list.add('}');
        return list.contains(c);
    }

    /**
     * ====================================================
     */


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btJudge:
                btJudge(v);
                break;
            case R.id.btContentProvider:
                intent = new Intent(this, Main2Activity.class);
                break;
            case R.id.btEncrypt:
                intent = new Intent(this, Main3Activity.class);
                break;
            case R.id.btDataBind:
                intent = new Intent(this, Main4Activity.class);
                break;
            case R.id.btNotification:
                // 通知测试
                intent = new Intent(this, Main5Activity.class);
                break;
            case R.id.btPermission:
                // 测试请求权限
                intent = new Intent(this, Main6Activity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
