package Order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import specification.OrderStep;
import specification.UserStep;
import stellarburgers.Ingredient;
import stellarburgers.IngredientData;
import stellarburgers.Order;
import stellarburgers.User;

import java.util.ArrayList;
import java.util.List;

import static constant.ConstantUrlApi.BASE_URL;
import static constant.ConstantUserData.*;

public class ChangingOrderTest {

    UserStep userStep = new UserStep();
    OrderStep orderStep = new OrderStep();
    User user = new User(USER_EMAIL, USER_PASSWORD, USER_NAME);
    private String accessToken;
    private List<String> ingred;
    private Order order;

    //Order order = new Order();
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        //RestAssured.filters(new AllureRestAssured());
        userStep.createUser(user);
        accessToken = userStep.accessTokenUser(user);
        ingred = new ArrayList<>();
        order = new Order(ingred);
    }

    @DisplayName("Создание заказа")
    @Description("с авторизацией")
    @Test
    public void creatingOrderAuthorizationTest() {
        IngredientData ingredientData = orderStep.getIngredients();
        ingred.add(ingredientData.getData().get(1).get_id());
        ingred.add(ingredientData.getData().get(7).get_id());
        ingred.add(ingredientData.getData().get(2).get_id());
        int sumPrice = ingredientData.getData().stream().filter(ingredient -> ingred.contains(ingredient.get_id()))
                .map(Ingredient::getPrice).mapToInt(i -> i).sum();
        Response response = orderStep.createOrder(order, accessToken);
        response.then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .assertThat().body("success", Matchers.is(true))
                .and().body("name", Matchers.notNullValue())
                .and().body("order.ingredients", Matchers.notNullValue())
                .and().body("order._id", Matchers.notNullValue())
                .and().body("order.owner.name", Matchers.is(USER_NAME))
                .and().body("order.owner.email", Matchers.is(USER_EMAIL))
                .and().body("order.status", Matchers.is("done"))
                .and().body("order.name", Matchers.notNullValue())
                .and().body("order.price", Matchers.is(sumPrice));
    }

    @After
    public void deleteUserTest() {
        if (accessToken != null) {
            userStep.deleteDataUser(accessToken);
        }
    }
}
