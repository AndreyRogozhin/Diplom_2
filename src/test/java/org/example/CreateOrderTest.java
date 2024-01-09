package org.example;


import com.google.gson.internal.LinkedTreeMap;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.example.UserGenerator.randomUser;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsEqual.equalTo;


public class CreateOrderTest {

    private String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private String ORDERS_URL = "/api/orders";
    private String INGRS_URL = "/api/ingredients";


    private Response response;
    private UserClient userClient;
    private User user;
    private Credentials credentials;
    private Credentials cred2;
    private Ingredients ingredients;
    private ArrayList<String> idsList;
    private String order;


    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;

        user = randomUser();
        userClient = new UserClient();
        response = userClient.create(user);

        credentials = user.credsFromUser();
        cred2 = response.body().as(Credentials.class);
        response = userClient.getIndredients();

        ingredients = response.body().as(Ingredients.class);

        idsList = new ArrayList<>();
        Object item;

        for (Object object : ingredients.data){
            item = ((LinkedTreeMap) object).get("_id");
            idsList.add(item.toString());
        }


    }



    @Test
    @Step("Создание заказа без авторизации - код возврата 200 и нет информации о клиенте")
    public void createOrderNoAthorisationStatus200NoOwner() {
        order = "{ \"ingredients\": \"" + idsList.get(0) + "\"}";
        response = userClient.createOrder(order,"");

        response.then()
                .statusCode(200)
                .and()
                .assertThat().body("order.owner", is(nullValue()));
    }


    @Test
    @Step("Создание заказа без авторизации и неверный хэш- код возврата 400")
    public void createOrderNoAthorisationBadHashStatus400() {
        order = "{ \"ingredients\": \"609646e4dc916e00276b2870\"}";
        response = userClient.createOrder(order,"");

        response.then()
                .statusCode(400)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", IsEqual.equalTo("One or more ids provided are incorrect"));
    }

    @Test
    @Step("Успешная авторизация - код возврата 200 и полная информация о клиенте")
    public void createOrderAthorisedUserStatus200DescribedOwner() {
            order = "{ \"ingredients\": \"" + idsList.get(0) + "\"}";
            response = userClient.createOrder(order,cred2.getAccessToken());

            response.then()
                    .statusCode(200)
                    .and()
                  .assertThat().body("order.owner.name", IsEqual.equalTo(credentials.getName()))
                    .and()
                    .assertThat().body("order.owner.email", IsEqual.equalTo(credentials.getEmail())) ;
    }


    @Test
    @Step("Создание заказа без списка ингредиентов - код возврата 400")
    public void createEmptyOrderStatus400(){

        order = "{ \"ingredients\": \"\"}";
        response = userClient.createOrder(order,"");

        response.then()
                .statusCode(400)
                .and()
                .assertThat().body("message", IsEqual.equalTo("Ingredient ids must be provided"));


    }

    @Test
    @Step("Создание заказа с неверным списком списка ингредиентов - код возврата 500")
    public void createWrongIngredientsStatus500(){

        order = "{ \"ingredients\": \"wrong list\"}";
        response = userClient.createOrder(order,"");

        response.then()
                .statusCode(500)
                .and()
                .assertThat().body( containsString("Internal Server Error"));


    }

    @After
    @Step("Удаление созданного клиента")
    public void tearDown() {
        response = userClient.delete(cred2.getAccessToken());
    }

}
