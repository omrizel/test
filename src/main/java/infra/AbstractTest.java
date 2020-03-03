package infra;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

public class AbstractTest {

    protected static ExtentReports extentReport;
    protected static String parentTestName;
    protected static String childTestName;
    protected static String suiteName;
    public static ExtentTest parentTest;
    public static ExtentTest childTest;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) {
        suiteName = context.getCurrentXmlTest().getSuite().getName();
        extentReport = ExtentManager.createInstance(suiteName);
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass() throws MalformedURLException {
        parentTestName = replaceCodeStrToHumanReadableStr(this.getClass().getName());
        parentTest = extentReport.createTest(parentTestName);
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method, ITestContext context, Object[] testData) {
        childTestName = replaceCodeStrToHumanReadableStr(method.getName());
        if (testData.length == 0){
            childTest = parentTest.createNode(childTestName);
        } else {
            childTest = parentTest.createNode(childTestName +" ("+testData[0]+")");
        }
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result, ITestContext context) {
        long time = result.getEndMillis() - result.getStartMillis();
        switch (result.getStatus()) {
            case (1):
                childTest.pass("passed in: " + time + " milliseconds");
                break;
            case (2):
                childTest.fail("failed in: " + time + " milliseconds, Exception: " + result.getThrowable().getMessage());
                break;
            case (3):
                childTest.skip("skipped");
                break;
            default:
                childTest.warning("test result is unclear, status code: " + result.getStatus());
        }
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {

    }

    @AfterTest(alwaysRun = true)
    public void afterTest() {

    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite(ITestContext testContext) {
        extentReport.flush();
    }

    public String replaceCodeStrToHumanReadableStr(String string) {
        String[] sampleArray = string.split("(?=\\p{Lu})");
        sampleArray = ArrayUtils.removeElement(sampleArray, "this");
        String newStringName = StringUtils.join(sampleArray, " ");
        newStringName = StringUtils.capitalize(newStringName);
        return newStringName;
    }

    public Response getResponse(String endpoints) {
        return given().get(APIConstants.BASE_URL + endpoints);
    }

    public Response getResponse(String endpoints, boolean timer) {
        return given().get(APIConstants.BASE_URL + endpoints);
    }


}
