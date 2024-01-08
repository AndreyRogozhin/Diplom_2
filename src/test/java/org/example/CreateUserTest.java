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


public class CreateUserTest {

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
    public void createCourierStatus200() {
        response = userClient.create(user);
        assertEquals("test fail!", 200, response.statusCode());
    }

    @Test
    @Step("Успешное создание пользователя - ответ OK")
    public void createCourierGetOK() {
        response = userClient.create(user);
        response.then().assertThat().body("success", equalTo(true));

    }


    @Test
    @Step("Повторное создание пользователя - код возврата 403")
    public void createUserTwiceStatus403() {
        response = userClient.create(user);
        response = userClient.create(user);
        assertEquals("test fail!", 403, response.statusCode());
    }


    @Test
    @Step("Повторное создание пользователя - сообщение об ошибке")
    public void createUserTwiceStatusFailed() {
        response = userClient.create(user);
        response = userClient.create(user);
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("User already exists"));
        ;
    }


    @Test
    @Step("Создание пользователя без email- код возврата 403")
    public void createCourierNoEmailStatus403() {
        user.setEmail("");
        response = userClient.create(user);

        response.then().statusCode(403);
    }



    @Test
    @Step("Создание пользователя без email- сообщение об ошибке")
    public void createCourierNoEmailStatusFailed() {
        user.setEmail("");
        response = userClient.create(user);
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }



    @Test
    @Step("Создание пользователя без пароля - код возврата 400")
    public void createCourierNoPasswordStatus400() {
        user.setPassword("");
        response = userClient.create(user);

        response.then().statusCode(400);

    }
/*
    @After
    public void tearDown() {
        credentials = user.credsFromCourier();
        response = userClient.login(credentials);
        Id id = response.body().as(Id.class);
        response = userClient.delete(id.getId());
    }
*/


}