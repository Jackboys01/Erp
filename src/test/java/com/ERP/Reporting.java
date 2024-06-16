package com.ERP;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class Reporting
{
    public static ExtentReports extent;
    public static ExtentTest test;
    public static ExtentHtmlReporter htmlReporter;
    public static void generateHtmlReport()
    {
        htmlReporter = new ExtentHtmlReporter("test-output/extent.html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    }
}
