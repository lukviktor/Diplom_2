package Order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
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

public class ChangingOrderTest {

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

    @DisplayName("Создание заказа")
    @Description("с авторизацией, с ингредиентами")
    @Test
    public void creatingOrderAuthorizationIngredientTest() {
        IngredientData ingredientData = orderStep.getIngredients();
        ingred.add(ingredientData.getData().get(0).get_id());
        ingred.add(ingredientData.getData().get(7).get_id());
        ingred.add(ingredientData.getData().get(2).get_id());
        orderStep.createOrder(order, accessToken)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .assertThat().body("success", Matchers.is(true))
                .and().body("name", Matchers.notNullValue())
                .and().body("order.ingredients", Matchers.notNullValue())
                .and().body("order.ingredients._id", Matchers.notNullValue())
                .and().body("order.owner.name", Matchers.is(USER_NAME))
                .and().body("order.owner.email", Matchers.is(USER_EMAIL))
                .and().body("order.status", Matchers.is("done"))
                .and().body("order.name", Matchers.notNullValue());
    }

    @DisplayName("Создание заказа")
    @Description("с авторизацией, без ингредиентами")
    @Test
    public void creatingOrderAuthorizationNotIngredientTest() {
        orderStep.createOrder(order, accessToken)
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .assertThat().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Ingredient ids must be provided"));
    }

    @DisplayName("Создание заказа")
    @Description("с авторизацией, с неверным хешем ингредиентов")
    @Test
    public void creatingOrderAuthorizationIncorrectHashIngredientTest() {
        IngredientData ingredientData = orderStep.getIngredients();
        ingred.add(ingredientData.getData().get(0).get_id() + "123");
        ingred.add(ingredientData.getData().get(7).get_id() + "456");
        ingred.add(ingredientData.getData().get(2).get_id() + "789");
        orderStep.createOrder(order, accessToken)
                .then().log().all()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @DisplayName("Создание заказа")
    @Description("без авторизации, с ингредиентами")
    @Test  // Не уверен, что ассерты верны. Предполагаю, что должно возвращать 401 и "You should be authorised"
    public void creatingOrderNotAuthorizationIngredientTest() {
        IngredientData ingredientData = orderStep.getIngredients();
        ingred.add(ingredientData.getData().get(1).get_id());
        ingred.add(ingredientData.getData().get(7).get_id());
        ingred.add(ingredientData.getData().get(2).get_id());
        orderStep.createOrder(order, "")
                .then().log().all()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .assertThat().body("message", Matchers.is("You should be authorised"));
    }

    @DisplayName("Создание заказа")
    @Description("без авторизацией, без ингредиентами")
    @Test
    public void creatingOrderNotAuthorizationNotIngredientTest() {
        orderStep.createOrder(order, "")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .assertThat().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Ingredient ids must be provided"));
    }

    @DisplayName("Создание заказа")
    @Description("без авторизацией, с неверным хешем ингредиентов")
    @Test
    public void creatingOrderNotAuthorizationIncorrectHashIngredientTest() {
        IngredientData ingredientData = orderStep.getIngredients();
        ingred.add(ingredientData.getData().get(0).get_id() + "123");
        ingred.add(ingredientData.getData().get(7).get_id() + "456");
        ingred.add(ingredientData.getData().get(2).get_id() + "789");
        orderStep.createOrder(order, "")
                .then().log().all()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void deleteUserTest() {
        if (accessToken != null) {
            userStep.deleteDataUser(accessToken);
        }
    }
}
