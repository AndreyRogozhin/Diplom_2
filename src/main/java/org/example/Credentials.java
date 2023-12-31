package org.example;

public class Credentials {

    private String email;
    private String password;
    private String name;
    private String accessToken;
    private String refreshToken;


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


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    public Credentials(String email, String password, String name) {
        this.password = password;
        this.name = name;
        this.email = email;

    }

    public Credentials() {}


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
