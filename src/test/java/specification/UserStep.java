package specification;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import stellarburgers.User;

import static constant.ConstantUrlApi.*;
import static io.restassured.RestAssured.given;

public class UserStep {

    @Step("Создание нового пользователя")
    public Response createUser(User user) {
        return given().log().all()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(ENDPOINT_REGISTER_USER);
    }
    @Step("получение токена пользователя accessToken")
    public String accessTokenUser(User user) {
        return loginUser(user).then().extract().path("accessToken");
    }

    @Step("Авторизация пользователя")
    public Response loginUser(User user) {
        return given().log().all()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(ENDPOINT_LOGIN_USER);
    }

    @Step("Удаление пользователя")
    public void deleteDataUser(String accessToken) {
        given().log().all()
                .header("Authorization", accessToken)
                .delete(ENDPOINT_DELETE_USER);
    }

@Step("Изменение данных пользователя")
public Response changingDataUser(User user, String accessToken) {
    Response authorization = given().log().all()
            .header("Content-type", "application/json")
            .header("Authorization", accessToken)
            .when()
            .body(user)
            .patch(ENDPOINT_DELETE_USER);
    return authorization;
}


}

