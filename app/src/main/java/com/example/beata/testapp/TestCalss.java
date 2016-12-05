package com.example.beata.testapp;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by huangbiyun on 16-12-2.
 */
public class TestCalss {

    public static void main(String[] args){

        final Map<String, LinkedList<Person>> linkedListMap = new HashMap<String, LinkedList<Person>>();

        final LinkedList<Person> list1 = new LinkedList<Person>();
        list1.add(new Person("a"));
        list1.add(new Person("b"));
        list1.add(new Person("c"));

        for(Person integer : list1){
            System.out.println(integer);
        }

        linkedListMap.put("a", new LinkedList<Person>(list1));

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                LinkedList<Person> mapList = linkedListMap.get("a");
                for(Person integer : mapList){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(integer);
                }
                System.out.println();
            }
        });
        thread.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        list1.remove(2);
        System.out.println("remove 2");

        /*LinkedList<Person> list2 = new LinkedList<Person>();
        list2.addAll(list1);
        list1.remove(0);

        System.out.println();
        for(Person integer : list2){
            System.out.println(integer);
        }*/

        for(Person integer : list1){
            System.out.println(integer);
        }

    }

    static class Person{
        String name;

        public Person(String name){
            this.name = name;
        }
    }


}
