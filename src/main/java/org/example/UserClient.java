package org.example;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import org.hamcrest.MatcherAssert;


public class UserClient {


    private String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private String USER_URL = "/api/auth/register";
    private String LOGIN_URL = "/api/auth/login";
    //private String DELETE_URL = "/api/v1/courier/";
    //private String ORDERS_URL = "/api/v1/orders";

@Step("Создание пользователя {user}")
    public Response create (User user){
    return             given()
                    .header("Content-type", "application/json")
                    .body(user)
                    .when()
                    .post(USER_URL);
    }




    @Step ("Авторизация курьера с учётными данными {credentials}")
    public Response login ( Credentials credentials, String token ) {
        return given()
                .header("Content-type", "application/json")
                .header("accessToken", token)
                .body(credentials)
                .when()
                .post(LOGIN_URL);
    }




    /*
    @Step ("Создание заказа с параметрам {order}")
    public Response createOrder (Order order){
        return             given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ORDERS_URL);
    }



    @Step ("Удаление курьера с ID {id}")
    public Response delete(int id) {
        return given()
                .header("Content-type", "application/json")
//                .body(courier)
                .when()
               .delete(DELETE_URL + id);
    }
*/

}
