package com.example.homework03_program1;

public class Major {

    private int id;
    private String name, prefix;

    public Major() {}

    public Major(String name, String prefix) {
        this.name = name;
        this.prefix = prefix;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return "{Major: " + id + ", " + name + ", " + prefix + "}";
    }
}
