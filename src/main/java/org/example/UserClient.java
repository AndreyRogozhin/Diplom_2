package org.example;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class UserClient {


    private final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private final String CREATE_URL = "/api/auth/register";
    private final String LOGIN_URL = "/api/auth/login";
    private final String EDIT_URL = "/api/auth/user";
    private final String ORDERS_URL = "/api/orders";
    private final String INGREDIENTS_URL = "/api/ingredients";

    @Step("Создание пользователя {user}")
    public Response create(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(CREATE_URL);
    }


    @Step("Авторизация пользователя с учётными данными {credentials}")
    public Response login(Credentials credentials, String token) {
        Response response = given()
                .header("Content-type", "application/json")
                .header("accessToken", token)
                .body(credentials)
                .when()
                .post(LOGIN_URL);

        return response;
    }


    @Step("Получение данных пользователля")
    public Response read(Credentials credentials, String token) {
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(credentials)
                .when()
                .get(EDIT_URL);

        return response;
    }


    @Step("Редактирование данных пользователля")
    public Response edit(Credentials credentials, String token) {
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(credentials)
                .when()
                .patch(EDIT_URL);
        return response;
    }


    @Step("Получение списка ингредиентов")
    public Response getIndredients() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .get(INGREDIENTS_URL);

        return response;
    }


    @Step("Создание заказа с параметрами {ingredients}")
//    public Response createOrder (Ingredients ingredients, String token){
    public Response createOrder(String order, String token) {

        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(order)
                .when()
                .post(ORDERS_URL);

        return response;
    }


    @Step("Получение списка заказов")
    public Response getOrders(String token) {

        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .when()
                .get(ORDERS_URL);

        return response;
    }


    @Step("Удаление клиента")
    public Response delete(String token) {

        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .when()
                .delete(EDIT_URL);

        return response;

    }


}
