package org.example;

public class User {

    private String password;
    private String name;
    private String email;

    public User() {
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public User(String email, String password, String name) {
        this.email = email + "@yandex.ru";
        this.password = password;
        this.name = name;
    }


    public Credentials credsFromUser(){
        return new Credentials(this.email, this.password, this.name);
    }



    }
