package org.example;


import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import static org.example.UserGenerator.randomUser;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;


public class CreateUserSuccessTest {

    private Response response;
    private UserClient userClient;
    private User user;
    private Credentials credentials;


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        user = randomUser();
        userClient = new UserClient();
    }

    @Test
    @Step("Успешное создание пользователя - код возврата 200")
    public void createUserStatus200() {
        response = userClient.create(user);
        assertEquals("test fail!", 200, response.statusCode());
    }

    @Test
    @Step("Успешное создание пользователя - ответ OK")
    public void createUserGetOK() {
        response = userClient.create(user);
        response.then().assertThat().body("success", equalTo(true));

    }



    @After
    @Step("Удаление созданного клиента")
    public void tearDown() {
        credentials = response.body().as(Credentials.class);
        response = userClient.delete(credentials.getAccessToken());
    }



}