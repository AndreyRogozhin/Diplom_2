package org.example;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.example.UserGenerator.randomUser;

public class LoginUserTest {


    private final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    private Response response;
    private UserClient userClient;
    private User user;
    private Credentials credentials;
    private String token;


    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;

        user = randomUser();
        userClient = new UserClient();
        response = userClient.create(user);
        credentials = user.credsFromUser();
        token = response.path("accessToken");


    }

    @Test
    @Step("Успешная авторизация - код возврата 200")
    public void loginUserSucessfullyStatus200() {
        response = userClient.login(credentials, token);
        response.then()
                .statusCode(200);
    }


    @Test
    @Step("Авторизация с указанием несуществующего логина - код возврата 400")
    public void loginUserWrongLoginStatus404() {
        Credentials credentialsNewEmail = new Credentials("xxx" + user.getEmail(), user.getPassword(), user.getName());
        response = userClient.login(credentialsNewEmail, token);
        response.then()
                .statusCode(401)
                .and()
                .assertThat().body("success", IsEqual.equalTo(false))
                .and()
                .assertThat().body("message", IsEqual.equalTo("email or password are incorrect"));
    }


    @After
    @Step("Удаление созданного клиента")
    public void tearDown() {
        response = userClient.delete(token);
    }

}
