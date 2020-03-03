package tests;

import infra.APIConstants;
import infra.AbstractTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class HeaderTests extends AbstractTest {

    @DataProvider(name = "endpoints")
    public Object[][] endPointTestData() {

        return new String[][]{
                {"people/1"},
                {"films/2"}
        };
    }

    @Test(dataProvider = "endpoints")
    public void responseStatusCodeValidation(String endpoints) {
        childTest.info("endpoint: " + APIConstants.BASE_URL + endpoints);
        Response r = getResponse(endpoints, true);
        Assert.assertEquals(r.getStatusCode(), 200);
        childTest.pass("status code is verified: 200");
    }

    @Test(dataProvider = "endpoints")
    public void responseContentTypeValidation(String endpoints) {
        childTest.info("endpoint: " + APIConstants.BASE_URL + endpoints);
        Response r = getResponse(endpoints, true);
        Assert.assertEquals(r.getContentType(), "application/json");
        childTest.pass("content type is verified: application/json");
    }

    @Test(dataProvider = "endpoints")
    public void responseTimeValidation(String endpoints) {
        childTest.info("endpoint: " + APIConstants.BASE_URL + endpoints);
        Response r = getResponse(endpoints, true);
        Assert.assertTrue(r.getTimeIn(TimeUnit.MILLISECONDS) < APIConstants.EXPECTED_RESPONSE_TIME);
        childTest.pass("response time is verified: under 6 seconds (swapi api is extremely slow)");
    }

    @Test
    public void validateResponseStatusCode404() {
        String endpoint = APIConstants.BASE_URL + "people/1000";
        childTest.info("endpoint: " + endpoint);
        Response r = getResponse(endpoint, true);
        Assert.assertEquals(r.getStatusCode(), 404);
        childTest.pass("response returned 404 error when calling resource which does not exist");
    }
}
