import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;

public class TestBase {
    @BeforeAll
    static void setup() {
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = "https://reqres.in/";
    }

    public boolean compareId(ArrayList<Integer> id) {
        System.out.println("-----------------------------------");
        boolean flag = true;
        for (int i = 1; i < id.size(); i++) {
            if (id.get(i) <= id.get(i - 1)) {
                flag = false;
                break;
            }
        }
        return flag;
    }
}
