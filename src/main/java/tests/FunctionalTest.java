package tests;

import infra.APIConstants;
import infra.AbstractTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.hasItem;

public class FunctionalTest extends AbstractTest {

    @DataProvider(name = "searchEndPoint")
    public Object[][] searchEndPointTestData() {

        return new String[][]{
                {"people/?search=luke", "Luke Skywalker", "name"},
                {"films/?search=hope", "A New Hope", "title"}
        };
    }

    @Test(dataProvider = "searchEndPoint")
    public void verifySearchResults(String searchEndPoint, String expectedItem, String expectedKey) {
        String endpoint = APIConstants.BASE_URL + searchEndPoint;
        AbstractTest.childTest.info("endpoint: " + endpoint);
        get(endpoint).then().assertThat()
                .body("results." + expectedKey, hasItem(expectedItem));
        AbstractTest.childTest.pass("search result for endpoint: " + endpoint + " including the expected key/value pair: " + expectedKey + ":" + expectedItem);
    }

    @DataProvider(name = "resultCount")
    public Object[][] resultCountTestData() {

        return new String[][]{
                {"people", "87"},
                {"films", "7"}
        };
    }

    @Test(dataProvider = "resultCount")
    public void verifyCountEqualOrAboveExpected(String endpoint, String expectedCount) {
        AbstractTest.childTest.info("endpoint: " + APIConstants.BASE_URL + endpoint);
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.get(APIConstants.BASE_URL + endpoint);
        String strResponse = response.getBody().asString();
        JSONObject jsonResponse = new JSONObject(strResponse);
        int count = (int) jsonResponse.get("count");
        if (count < Integer.parseInt(expectedCount)) {
            Assert.fail("count is below expected: expected: " + expectedCount + " actual: " + count + " (endpoint: " + endpoint + ")");
        }
        AbstractTest.childTest.pass("result count is equal or above the expected: " + endpoint + ":" + count);
    }

    @DataProvider(name = "pagination")
    public Object[][] apiPaginationCases() {

        return new String[][]{
                {"people/?page=1"}, //only next, previous null
                {"people/?page=2"}, // next & previous
                {"people/?page=9"} // only previous, next null
        };
    }

    @Test(dataProvider = "pagination")
    public void verifyApiPagination(String endpoint) {
        AbstractTest.childTest.info("endpoint: " + APIConstants.BASE_URL + endpoint);
        int page = Integer.valueOf(endpoint.substring(endpoint.length() - 1));
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.get(APIConstants.BASE_URL + endpoint);
        String strResponse = response.getBody().asString();
        JSONObject jsonResponse = new JSONObject(strResponse);
        Object next = jsonResponse.get("next");
        Object previous = jsonResponse.get("previous");
        switch (page) {
            case 1:
                Assert.assertEquals(next, APIConstants.BASE_URL + "people/?page=2");
                if (previous.toString() != "null") {
                    Assert.fail("previous is expected to be null");
                }
                AbstractTest.childTest.pass("verified people result page 1 has next page:people/?page=2 and previous is null");
                break;
            case 2:
                Assert.assertEquals(next, APIConstants.BASE_URL + "people/?page=3");
                Assert.assertEquals(previous, APIConstants.BASE_URL + "people/?page=1");
                AbstractTest.childTest.pass("verified people result page 2 has next page:people/?page=3 and previous page:people/?page=1");
                break;
            case 9:
                if (next.toString() != "null") {
                    Assert.fail("next is expected to be null");
                }
                Assert.assertEquals(previous, APIConstants.BASE_URL + "people/?page=8");
                AbstractTest.childTest.pass("verified people result page 9 next is null and and previous page:people/?page=8");
                break;
            default:
                Assert.fail("data is not supported");
                break;
        }
    }
}
