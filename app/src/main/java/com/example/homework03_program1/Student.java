package com.example.homework03_program1;

public class Student {

    private String firstName, lastName, username, email;
    private int age;
    private double gpa;
    private Major major;

    public Student() {}

    public Student(String f, String l, String u, String e, int a, double g, Major m) {
        this.firstName = f;
        this.lastName = l;
        this.username = u;
        this.email = e;
        this.age = a;
        this.gpa = g;
        this.major = m;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    @Override
    public String toString() {
        return "{Student: " + firstName + " " + lastName + ", " + username + ", " + email + ", " + age + ", " + gpa + ", " + major.toString() + "}";
    }
}
