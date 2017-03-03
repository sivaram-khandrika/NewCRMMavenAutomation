package CRMAutomation.CRMAutomation;


import org.openqa.selenium.*;
//import org.apache.poi.xssf.*;


//import org.openqa.selenium.in

public interface IGlobalMethods {
	public WebDriver createWebDriver(String typeDriver);
	public void navigateURL(WebDriver driver, String url);
	public String createResultFolder();
	public void createResultFile(String path, String workSheet);
	public void writeResults(String path, String testCaseID,String testCaseName,String expectedResult,String result, String sheetName, String uniqueValue);
	public String getResultFromResultFile(String path, String workSheet);
	public String generateDateTime();
	public String fileExists(String path);
}
