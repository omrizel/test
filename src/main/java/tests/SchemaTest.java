package tests;

import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import infra.APIConstants;
import infra.AbstractTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class SchemaTest extends AbstractTest {



    @DataProvider(name = "schema")
    public Object[][] createSchemaTestData() {

        return new String[][]{
                {"expectedPeopleSchema.json", "people"},
                {"expectedFilmsSchema.json", "films"},
                {"wrongPeopleSchema.json", "films"} //failed example
        };
    }

    @Test(dataProvider = "schema")
    public void schemaTest(String schema, String endpoint) {
        childTest.info("endpoint: " + APIConstants.BASE_URL + endpoint);
        JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.newBuilder()
                .setValidationConfiguration(ValidationConfiguration
                        .newBuilder()
                        .setDefaultVersion(SchemaVersion.DRAFTV4)
                        .freeze())
                .freeze();
        get(APIConstants.BASE_URL + endpoint + "/1").then().assertThat()
                .body(matchesJsonSchemaInClasspath(schema)
                        .using(jsonSchemaFactory));
        childTest.pass("the " + endpoint + " response schema is identical to the expected schema (structure/data types) which can be found on the resource folder under the name: " + schema);
    }
}
