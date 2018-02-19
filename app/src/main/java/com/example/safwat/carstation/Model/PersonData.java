package com.example.safwat.carstation.Model;

/**
 * Created by safwat on 14/06/17.
 */

public class PersonData {
    private String name;
    private int id;

    public PersonData(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
