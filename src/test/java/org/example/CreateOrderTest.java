package org.example;


import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;

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
    private Ingredients ids;


    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;

        user = randomUser();
        userClient = new UserClient();
        response = userClient.create(user);
        ids = new Ingredients();
}



    @Test
    @Step("Создание заказа без авторизации - код возврата 200 и нет информации о клиенте")
    public void createOrderNoAthorisationStatus200NoOwner() {
        credentials = user.credsFromUser();
        Credentials cred2 = response.body().as(Credentials.class);

        ids.setIngredients("61c0c5a71d1f82001bdaaa6d");
        String order = "{ \"ingredients\":\"" + ids.getIngredients() + "\"}";
        response = userClient.createOrder(order,"");

        response.then()
                .statusCode(200)
                .and()
                .assertThat().body("order.owner", is(nullValue()));
    }


    @Test
    @Step("Создание заказа без авторизации и неверный хэш- код возврата 400")
    public void createOrderNoAthorisationBadHashStatus400() {
        credentials = user.credsFromUser();
        Credentials cred2 = response.body().as(Credentials.class);

        ids.setIngredients("609646e4dc916e00276b2870");
        String order = "{ \"ingredients\":\"" + ids.getIngredients() + "\"}";
        response = userClient.createOrder(order,"");

        response.then()
                .statusCode(400)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", IsEqual.equalTo("One or more ids provided are incorrect"));
    }
    /*
    {
    "success": false,
    "message": "One or more ids provided are incorrect"
}
     */




    @Test
        @Step("Успешная авторизация - код возврата 200 и полная информация о клиенте")
        public void createOrderAthorisedUserStatus200DescribedOwner() {
            credentials = user.credsFromUser();
            Credentials cred2 = response.body().as(Credentials.class);
            //String hhh = response.;
            //response = userClient.createOrder(credentials,cred2.getAccessToken());

            ids.setIngredients("61c0c5a71d1f82001bdaaa6d");
            String order = "{ \"ingredients\":\"" + ids.getIngredients() + "\"}";
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
        //        credentials = user.credsFromUser();
        //Credentials cred2 = response.body().as(Credentials.class);
        //String hhh = response.;

        ids.setIngredients("");
        String order = "{ \"ingredients\":\"" + ids.getIngredients() + "\"}";
        response = userClient.createOrder(order,"");

        response.then()
                .statusCode(400)
                .and()
                .assertThat().body("message", IsEqual.equalTo("Ingredient ids must be provided"));


    }

    @Test
    @Step("Создание заказа с неверным списком списка ингредиентов - код возврата 500")
    public void createWrongIngredientsStatus500(){
        //        credentials = user.credsFromUser();
        //Credentials cred2 = response.body().as(Credentials.class);
        //String hhh = response.;

        ids.setIngredients("wrong list");
        String order = "{ \"ingredients\":\"" + ids.getIngredients() + "\"}";
        response = userClient.createOrder(order,"");

        response.then()
                .statusCode(500)
                .and()
                .assertThat().body( containsString("Internal Server Error"));


    }



    /*
    @Test
    @Step("Создание заказа без списка ингредиентов - код возврата 400")
        public void createEmptyOrderStatus400(){
        //        credentials = user.credsFromUser();
        //Credentials cred2 = response.body().as(Credentials.class);
        //String hhh = response.;

        ids.setIngredients("");
        String order = "{ \"ingredients\":\"" + ids.getIngredients() + "\"}";
        response = userClient.createOrder(order,"");

        response.then()
                .statusCode(400)
                .and()
                .assertThat().body("message", IsEqual.equalTo("Ingredient ids must be provided"));


    }
*/

}
