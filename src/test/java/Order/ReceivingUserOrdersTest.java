package Order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import jdk.jfr.Description;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import specification.OrderStep;
import specification.UserStep;
import stellarburgers.IngredientData;
import stellarburgers.Order;
import stellarburgers.User;

import java.util.ArrayList;
import java.util.List;

import static constant.ConstantUrlApi.BASE_URL;
import static constant.ConstantUserData.*;

public class ReceivingUserOrdersTest {
    /*
    Получение заказов конкретного пользователя:
авторизованный пользователь,
неавторизованный пользователь
     */

    private UserStep userStep = new UserStep();
    private OrderStep orderStep = new OrderStep();
    private User user = new User(USER_EMAIL, USER_PASSWORD, USER_NAME);
    private String accessToken;
    private List<String> ingred = new ArrayList<>();
    private Order order = new Order(ingred);

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        //RestAssured.filters(new AllureRestAssured());
        userStep.createUser(user);
        accessToken = userStep.accessTokenUser(user);
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя")
    @Description("авторизованный пользователь, без созданного заказа")
    public void checkAuthorizationUserOrderTest() {
        orderStep.getOrderUser(accessToken)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .and().assertThat().body("success", Matchers.is(true))
                .and().body("orders", Matchers.notNullValue())
                .and().body("total", Matchers.notNullValue())
                .and().body("totalToday", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя")
    @Description("авторизованный пользователь и созданным заказом")
    public void checkAuthorizationUserGenerateOrderTest() {
        IngredientData ingredientData = orderStep.getIngredients();
        ingred.add(ingredientData.getData().get(2).get_id());

        orderStep.createOrder(order, accessToken);
        orderStep.getOrderUser(accessToken)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .and().assertThat().body("success", Matchers.is(true))
                .and().body("orders", Matchers.notNullValue())
                .and().body("orders._id", Matchers.notNullValue())
                .and().body("orders.ingredients", Matchers.notNullValue())
                .and().body("orders.status", Matchers.notNullValue())
                .and().body("orders.name", Matchers.notNullValue())
                .and().body("orders.ingredients", Matchers.notNullValue())
                .and().body("orders.createdAt", Matchers.notNullValue())
                .and().body("orders.updatedAt", Matchers.notNullValue())
                .and().body("orders.number", Matchers.notNullValue())
                .and().body("total", Matchers.notNullValue())
                .and().body("totalToday", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя")
    @Description("неавторизованный пользователь")
    public void checkNotAuthorizationUserOrderTest() {
        orderStep.getOrderUser("")
                .then().log().all()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and().assertThat().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }

    @After
    public void deleteUserTest() {
        if (accessToken != null) {
            userStep.deleteDataUser(accessToken);
        }
    }
}
