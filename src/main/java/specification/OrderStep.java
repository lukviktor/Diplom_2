package specification;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import stellarburgers.IngredientData;
import stellarburgers.Order;

import static constant.ConstantUrlApi.ENDPOINT_ORDER;
import static io.restassured.RestAssured.given;

public class OrderStep {

    @Step("Получение данных об ингредиентах")
    public IngredientData getIngredients() {
        return given()
                .header("Content-Type", "application/json")
                .log().all()
                .get("api/ingredients")
                .body()
                .as(IngredientData.class);
    }

    @Step("Создание заказа")
    public Response createOrder(Order order, String accessToken) {
        return given().log().all()
                .header("Content-Type", "application/json")
                .header("authorization", accessToken)
                .body(order)
                .when()
                .post(ENDPOINT_ORDER);
    }
    @Step("Получение заказов пользователя")
    public Response getOrderUser(String accessToken){
        return given().log().all()
                .header("Content-Type", "application/json")
                .header("authorization", accessToken)
                .when()
                .get(ENDPOINT_ORDER);
    }
}
