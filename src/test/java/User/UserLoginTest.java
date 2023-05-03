package User;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import specification.UserStep;
import stellarburgers.User;

import static constant.ConstantUrlApi.BASE_URL;
import static constant.ConstantUserData.*;

public class UserLoginTest {
    UserStep userStep = new UserStep();
    User user = new User(USER_EMAIL, USER_PASSWORD, USER_NAME);
    private final String randomEmail = RandomStringUtils.randomAlphabetic(5) + "@yopmail.com";
    private final String randomPassword = RandomStringUtils.randomAlphabetic(15);
    private final String randomName = RandomStringUtils.randomAlphabetic(8);

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.filters(new AllureRestAssured());
        userStep.createUser(user);
    }

    @DisplayName("Логин пользователя")
    @Description("логин под существующим пользователем")
    @Test
    public void authorizationUserTest() {
        userStep.loginUser(user)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .and().assertThat().body("success", Matchers.is(true))
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue());
    }

    @DisplayName("Логин пользователя")
    @Description("логин с неверным логином и паролем (password)")
    @Test
    public void authorizationUserIncorrectPasswordTest() {
        user.setPassword(randomPassword);
        userStep.loginUser(user)
                .then().log().all()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and().assertThat().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }

    @DisplayName("Логин пользователя")
    @Description("логин с неверным логином и паролем (password null)")
    @Test
    public void authorizationUserIncorrectPasswordNullTest() {
        user.setPassword(randomPassword);
        userStep.loginUser(user)
                .then().log().all()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and().assertThat().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }

    @DisplayName("Логин пользователя")
    @Description("логин с неверным логином и паролем (email)")
    @Test
    public void authorizationUserIncorrectEmailTest() {
        user.setEmail(randomEmail);
        userStep.loginUser(user)
                .then().log().all()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and().assertThat().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }

    @DisplayName("Логин пользователя")
    @Description("логин с неверным логином и паролем (email null)")
    @Test
    public void authorizationUserIncorrectEmailNullTest() {
        user.setEmail("");
        userStep.loginUser(user)
                .then().log().all()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and().assertThat().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }

    @DisplayName("Логин пользователя")
    @Description("логин с неверным именем (name)")
    @Test
    public void authorizationUserIncorrectNameTest() {
        user.setName(randomName);
        userStep.loginUser(user)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .assertThat().body("success", Matchers.is(true));
    }

    @After
    public void deleteUserTest() {
        User user = new User(USER_EMAIL, USER_PASSWORD, USER_NAME);
        String accessToken = userStep.accessTokenUser(user);
        if (accessToken != null) {
            userStep.deleteDataUser(accessToken);
        }
    }
}
