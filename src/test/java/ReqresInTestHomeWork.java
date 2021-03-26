import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.junit.jupiter.api.Test;

import java.util.*;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


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
        Specifications.installSpec(Specifications.requestSpec(), Specifications.responseSpec());
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
}



