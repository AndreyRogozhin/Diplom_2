package org.example;


import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.example.UserGenerator.randomUser;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsEqual.equalTo;

public class UserOrdersTest {
    private String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private String ORDERS_URL = "/api/orders";
    private String INGRS_URL = "/api/ingredients";

    private Response response;
    private UserClient userClient;
    private User user;
    private Credentials credentials;
    private Ingredients ids;
    Credentials cred2;


    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;

        user = randomUser();
        userClient = new UserClient();
        response = userClient.create(user);
        //ids = new Ingredients();
        cred2 = response.body().as(Credentials.class);


    }



    @Test
    @Step("Успешная авторизация - код возврата 200")
    public void getOrdersAthorisedUserStatus200() {

        response = userClient.getOrders(cred2.getAccessToken());

        response.then()
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }


    @Test
    @Step("Нет авторизации - код возврата 401")
    public void getOrdersNoAthorisationStatus401() {

        response = userClient.getOrders("");

        response.then()
                .statusCode(401)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", IsEqual.equalTo("You should be authorised"));
    }

    @After
    @Step("Удаление созданного клиента")
    public void tearDown() {
        response = userClient.delete(cred2.getAccessToken());
    }

}
