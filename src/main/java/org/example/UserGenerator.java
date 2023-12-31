package org.example;

import static org.example.Utils.randomString;

public class UserGenerator {

    public static User randomUser(){
        return new User(randomString(8), randomString(16), randomString(6));
    }

    public static User givenUser(String login){
        return new User(login, randomString(16), randomString(6));
//        return new User(login, "myPassw", "myName");
    }

}
