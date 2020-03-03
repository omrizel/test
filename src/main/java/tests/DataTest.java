package tests;

import infra.APIConstants;
import infra.AbstractTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;

public class DataTest extends AbstractTest {

    @DataProvider(name = "endpointData")
    public Object[][] endpointData() {

        return new String[][]{
                {"people/1", "name", "Luke Skywalkerr"}, //failed test example - expected data is wrong on purpose
                {"people/2", "name", "C-3PO"},
                {"people/3", "name", "R2-D2"},
                {"people/4", "name", "Darth Vader"},
                {"people/5", "name", "Leia Organa"},
                {"films/1", "title", "A New Hope"},
                {"films/2", "title", "The Empire Strikes Back"},
                {"films/3", "title", "Return of the Jedi"},
                {"films/4", "title", "The Phantom Menace"},
                {"films/5", "title", "Attack of the Clones"}
        };
    }

    @Test(dataProvider = "endpointData")
    public void verifyEndpointData(String relativeEndpoint, String expectedKey, String expectedItem) {
        String endpoint = APIConstants.BASE_URL + relativeEndpoint;
        AbstractTest.childTest.info("endpoint: " + endpoint);
        get(endpoint).then().assertThat()
                .body(expectedKey, equalTo(expectedItem));
        AbstractTest.childTest.pass("endpoint data varification confirmed: " + expectedKey + " = "+expectedItem);
    }
}
