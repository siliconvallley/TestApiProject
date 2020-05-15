package com.dh.testproject.algorithm_utils;

import android.util.Log;

import com.dh.testproject.entity.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * 广度优先搜索（图）
 */
public class BreadthFirstSearch {
    private static final String TAG = "BreadthFirstSearch";

    public static String breadthFirstSearch(String searchName) {
        Map<String, Person[]> searchMap = saveMap();
        Queue<Person> queue = new LinkedList<>();
        List<Person> searchList = new ArrayList<>();

        Person[] searchArr = searchMap.get(searchName);
        if (searchArr == null || searchArr.length == 0) {
            return "";
        }
        for (Person person : searchArr) {
            queue.offer(person);
        }
        while (!queue.isEmpty()) {
            Person person = queue.poll();
            if (person == null) return "";
            if (!searchList.contains(person)) {
                if (person.isBusiness) {
                    Log.e(TAG, "姓名: " + person.name);
                    return person.name;
                } else {
                    Person[] peoples = searchMap.get(person.name);
                    if (peoples != null && peoples.length !=0 ) {
                        for (Person people : peoples) {
                            queue.offer(people);
                        }
                    }
                    // 将已经搜索过的人保存在已搜索集合中
                    searchList.add(person);
                }
            }
        }
        return "";
    }


    private static Map<String, Person[]> saveMap() {
        Map<String, Person[]> map = new HashMap<>();


        Person[] persons1 = {
                new Person("alice", false),
                new Person("bob", false),
                new Person("claire", false)};
        map.put("me", persons1);

        Person[] persons2 = {
                new Person("anuj", false),
                new Person("peggy", true)};
        map.put("bob", persons2);

        Person[] persons3 = {new Person("peggy", false)};
        map.put("alice", persons3);

        Person[] persons4 = {
                new Person("thom", false),
                new Person("jonny", false)};
        map.put("claire", persons4);

        Person[] person5 = {};
        map.put("anuj", person5);

        Person[] person6 = {};
        map.put("peggy", person6);

        Person[] person7 = {};
        map.put("thom", person7);

        Person[] person8 = {};
        map.put("jonny", person8);
        return map;
    }
}
