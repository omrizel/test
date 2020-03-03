package infra;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ExtentManager {

    protected static String path;
    private static ExtentReports extent;

    public static ExtentReports createInstance(String suiteName) {
        extent = new ExtentReports();
        path = System.getProperty("user.dir") + "/reports/" + ReportName(suiteName) + ".html";
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(path);

        htmlReporter.config().setDocumentTitle("Automation Report");
        htmlReporter.config().setReportName(suiteName);
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setTimeStampFormat("dd/MM/yyyy HH:mm:ss");

        extent.attachReporter(htmlReporter);
        return extent;
    }


    private static String ReportName(String suiteName) {
        return suiteName + "-" + getCurrentDateTime();
    }

    private static String getCurrentDateTime() {
        return new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(Calendar.getInstance().getTime());
    }


}
