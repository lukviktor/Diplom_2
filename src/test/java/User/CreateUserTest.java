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
import randomdata.RandomEmailUser;
import specification.UserStep;
import stellarburgers.User;

import static constant.ConstantUrlApi.BASE_URL;

public class CreateUserTest {
    RandomEmailUser emailUser = new RandomEmailUser();
    UserStep userStep = new UserStep();
    private String email = emailUser.randomEmailUser();


    private String password = RandomStringUtils.randomAlphanumeric(10);
    private String name = RandomStringUtils.randomAlphabetic(8);
    User user = new User(email, password, name);

    @Test
    public void dsa() {
        System.out.println(email);
        System.out.println(password);
        System.out.println(name);

    }

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.filters(new AllureRestAssured());
    }

    @DisplayName("Создание пользователя")
    @Description("создать уникального пользователя")
    @Test
    public void creatingUserTest() {
        userStep.createUser(user)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .and().assertThat().body("success", Matchers.is(true))
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue());
    }

    @DisplayName("Создание пользователя")
    @Description("создать пользователя, который уже зарегистрирован")
    @Test
    public void createIdenticalUsersTest() {
        userStep.createUser(user);
        userStep.createUser(user)
                .then().log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and().assertThat().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("User already exists"));
    }

    @DisplayName("Создание пользователя")
    @Description("создать пользователя и не заполнить одно из обязательных полей (email)")
    @Test
    public void createUsersIncorrectEmailTest() {
        user.setEmail("");
        userStep.createUser(user)
                .then().log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and().assertThat().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Email, password and name are required fields"));
    }

    @DisplayName("Создание пользователя")
    @Description("создать пользователя и не заполнить одно из обязательных полей (name)")
    @Test
    public void createUsersIncorrectNameTest() {
        user.setName("");
        userStep.createUser(user)
                .then().log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and().assertThat().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Email, password and name are required fields"));
    }

    @DisplayName("Создание пользователя")
    @Description("создать пользователя и не заполнить одно из обязательных полей (password)")
    @Test
    public void createUsersIncorrectPasswordTest() {
        user.setPassword("");
        userStep.createUser(user)
                .then().log().all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and().assertThat().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Email, password and name are required fields"));
    }

    @After
    public void deleteUserTest() {
        String accessToken = userStep.accessTokenUser(user);
        if (accessToken != null) {
            userStep.deleteDataUser(accessToken);
        }
    }
}
