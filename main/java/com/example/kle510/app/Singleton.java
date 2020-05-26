package com.example.kle510.app;

import java.util.HashSet;


public class Singleton {

    private HashSet<String> favorites = new HashSet<>();

    private static Singleton instance;

    private Singleton() {
        // nothing to do this time
    }

    static {
        instance = new Singleton();
    }

    public static Singleton getInstance() {
        return instance;
    }


    public boolean add (String id){
        if (favorites.contains(id) == false){
            favorites.add(id);
            return true;
        }
        return false;
    }

    public boolean contains (String id){
        if (favorites.contains(id)){
            return true;
        }
        return false;
    }

    public boolean remove (String id){
        if (favorites.contains(id)){
            favorites.remove(id);
            return true;
        }
        return false;
    }


}
