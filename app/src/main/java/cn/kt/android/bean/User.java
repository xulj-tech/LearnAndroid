package cn.kt.android.bean;

import java.io.Serializable;

public class User implements Serializable{

    public String name;
    public int age;
    public String gender;
    public String address;

    public User(String name, int age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

}
