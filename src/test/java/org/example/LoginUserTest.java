package org.example;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import static io.restassured.RestAssured.*;
import static org.example.UserGenerator.randomUser;
import static org.example.UserGenerator.randomUser;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.hamcrest.MatcherAssert;

public class LoginUserTest {


    private String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private String LOGIN_URL = "/api/user/login";

    private Response response;
    private UserClient userClient;
    private User user;
    private Credentials credentials;


    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;

        user = randomUser();
        userClient = new UserClient();
        response = userClient.create(user);


    }

    @Test
    @Step("Успешная авторизация - код возврата 200")
    public void loginUserSucessfullyStatus200() {
        credentials = user.credsFromUser();
        Credentials cred2 = response.body().as(Credentials.class);
        //String hhh = response.;
        response = userClient.login(credentials,cred2.getAccessToken());
        response.then()
                .statusCode(200);
    }



    @Test
    @Step("Авторизация с указанием несуществующего логина - код возврата 400")
    public void loginCourierWrongLoginStatus404() {

        Credentials credentialsNewEmail = new Credentials(user.getEmail() + "xxx", user.getPassword(), user.getName());
    //    response = userClient.login(credentialsNewEmail, token);
        response.then()
                .statusCode(404);
    }

/*

    @After
    @Step("Удаление созданного клиента")
    public void tearDown() {
        credentials = courier.credsFromCourier();
        response = courierClient.login(credentials);
        Id id = response.body().as(Id.class);
        response = courierClient.delete(id.getId());

    }

 */
}
