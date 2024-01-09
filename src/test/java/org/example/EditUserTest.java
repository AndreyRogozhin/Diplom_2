package org.example;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.example.UserGenerator.randomUser;
import static org.hamcrest.core.IsEqual.equalTo;

public class EditUserTest {

    private String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private String EDIT_URL = "/api/auth/user";

    private Response response;
    private UserClient userClient;
    private User user;
    private Credentials credentials, cred2;


    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;

        user = randomUser();
        userClient = new UserClient();
        response = userClient.create(user);

        credentials = user.credsFromUser();
        cred2 = response.body().as(Credentials.class);
        response = userClient.login(credentials,cred2.getAccessToken());

    }

    @Test
    @Step("Успешное редактирование - код возврата 200")
    public void editAuthorisedUserStatus200() {
        credentials.setName("newUserName");
        response = userClient.edit(credentials,cred2.getAccessToken());

        response.then().statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("user.name", equalTo("newUserName"));
    }


    @Test
    @Step("Редактирование без авторизации - код возврата 401")
    public void editNotAuthorisedUserStatus401() {
        credentials.setName("New Username");
        response = userClient.edit(credentials,"");

        response.then().statusCode(401)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("You should be authorised"));
    }


    @After
    @Step("Удаление созданного клиента")
    public void tearDown() {
        response = userClient.delete(cred2.getAccessToken());
    }
}






