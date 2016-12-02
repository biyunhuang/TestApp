package com.example.beata.testapp;

import java.util.ArrayList;

/**
 * Created by huangbiyun on 16-12-2.
 */
public class TestCalss {

    public static void main(String[] args){
        ArrayList<Person> list1 = new ArrayList<Person>();
        list1.add(new Person("a"));
        list1.add(new Person("b"));
        list1.add(new Person("c"));

        System.out.println();
        for(Person integer : list1){
            System.out.println(integer);
        }

        ArrayList<Person> list2 = new ArrayList<Person>(list1);
        list1.remove(0);

        System.out.println();
        for(Person integer : list2){
            System.out.println(integer);
        }

        System.out.println();
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
