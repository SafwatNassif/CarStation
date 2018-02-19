package com.example.safwat.carstation.Model;

/**
 * Created by safwat on 21/08/17.
 */

public class User {
    private String name,password,token,impass,phone;
    private int id;


    public User(String name, int id,String password,String token,String impass,String phone ) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.token = token;
        this.impass = impass;
        this.phone = phone;
     }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImpass() {
        return impass;
    }

    public void setImpass(String impass) {
        this.impass = impass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
