import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.junit.jupiter.api.Test;

import java.util.*;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class ReqresInTestHomeWork extends TestBase {

    @Step("Получение списка User, проверка ссылки avatar")
    @Test
    public void listUsersTestCheckUserAvatar() {
        Response response =
                given()
                        .contentType("application/json")
                        .when()
                        .get("/api/users?page=2")
                        .then()
                        .statusCode(200)
                        .extract().response();

        JsonPath jsonPath = response.jsonPath();
        List<HashMap<String, Object>> data = jsonPath.getList("data");
        for (HashMap<String, Object> singleObject : data) {
            System.out.print(singleObject.get("id") + "  ");
            System.out.print(singleObject.get("first_name") + "  ");
            String s = singleObject.get("avatar").toString().substring(0, 28);
            System.out.println(s);
            assertThat(s, equalTo("https://reqres.in/img/faces/"));
        }

    }

    @Step("Получение списка User, проверка списка по возрастанию Id")
    @Test
    public void listUsersTestCheckUserId() {
        Response response =
                given()
                        .spec(Specifications.requestSpec())
                        .when()
                        .get("/api/users?page=2")
                        .then()
                        .spec(Specifications.responseSpec())
                        .extract().response();

        JsonPath jsonPath = response.jsonPath();
        List<HashMap<String, Object>> data2 = jsonPath.getList("data");
        ArrayList<Integer> id = new ArrayList();
        for (HashMap<String, Object> singleObjectId : data2) {
            id.add((Integer) singleObjectId.get("id"));
        }
        System.out.println("-----------------------------------");
        System.out.println("Id в data " + id.toString());
        String flag = compareId(id) ? "Id записаны в data по возрастанию" : "Id записаны в data в произвольном порядке";
        System.out.println(flag);
    }

    @Step("Успешная регистрация email/password")
    @Test
    public void registrationTest() {
        Map<String, String> data = new HashMap<>();
        data.put("email", "eve.holt@reqres.in");
        data.put("password", "pistol");
        Response response = given()
                .spec(Specifications.requestSpec())
                .body(data)
                .when()
                .post("/api/register")
                .then()
                .spec(Specifications.responseSpec())
                .log().all()
                .extract().response();
        JsonPath jsonResponse = response.jsonPath();
        assertThat(jsonResponse.get("id"), equalTo(4));
        assertThat(jsonResponse.get("token"), equalTo("QpwL5tke4Pnpja7X4"));
    }

    @Step("Не успешная регистрация - error Missing email or username")
    @Test
    public void negativeRegistrationTest() {
        Map<String, String> data = new HashMap<>();
        data.put("email", "sydney@fife");
        Response response = given()
                .body(data)
                .spec(Specifications.requestSpec())
                .when()
                .post("/api/register")
                .then()
                .statusCode(400)
                .log().all()
                .extract().response();
        JsonPath jsonResponse = response.jsonPath();
        assertThat(jsonResponse.get("error"), equalTo("Missing email or username"));
    }


    @Step("Успешное создание User")
    @Test
    public void createUser() {
        Map<String, String> data = new HashMap<>();
        data.put("name", "Maria");
        data.put("job", "Engineer");
        Response response = given()
                .body(data)
                .spec(Specifications.requestSpec())
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .log().all()
                .extract().response();
        JsonPath jsonResponse = response.jsonPath();
        System.out.println((String) jsonResponse.get("name"));
        System.out.println((String) jsonResponse.get("job"));
        System.out.println((String) jsonResponse.get("id"));
        System.out.println((String) jsonResponse.get("createdAt"));

        assertThat(jsonResponse.get("id"), notNullValue());
    }
}




