package CRMAutomation.CRMAutomation;

//import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.util.Properties;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public class GlobalMethods implements IGlobalMethods{

	//public XSSFSheet sheet;
	public WebDriver createWebDriver(String typeDriver) {
		WebDriver cWebDriver;
		if(typeDriver.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", "C:/Selenium/geckodriver-v0.11.1-win64/geckodriver.exe");
			cWebDriver = new FirefoxDriver();
		} else if (typeDriver.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "C:/Selenium/chromedriver_win32/chromedriver.exe");
			cWebDriver = new ChromeDriver();
		} else {
			System.setProperty("web.ie.driver", "C:/Selenium/IEDriverServer.exe");
			cWebDriver = new InternetExplorerDriver();
		}
		return cWebDriver;
		
	}
	public void navigateURL(WebDriver driver, String url) {
		driver.get(url);
	}
	public String createResultFolder() {
		String location = System.getProperty("user.dir");
		String filePath = location + "\\result";
		File file = new File(filePath);
		if(file.exists()==false){ 
			System.out.println("Folder is not created");
			file.mkdirs(); 
      } 
		//System.out.println(filePath);
		return filePath;
	}
	public void createResultFile(String path, String workSheet){
		String fileCreation;
		try {
			//FileInputStream file = new FileInputStream(path);
			String fileStatus = fileExists(path);
			if (fileStatus.equalsIgnoreCase("exists")){
				System.out.println("File already created");
			}else {
				System.out.println("In createResultFile(String path, String workSheet)"+path +":" + workSheet);
				XSSFWorkbook wb = new XSSFWorkbook();
				XSSFSheet sheet = wb.createSheet(workSheet);
				XSSFRow row = sheet.createRow(0);
				row.createCell(0).setCellValue("TCID");
				row.createCell(1).setCellValue("TCName");
				row.createCell(2).setCellValue("ExpectedResult");
				row.createCell(3).setCellValue("ActualResult");
				row.createCell(4).setCellValue("UniqueValue");
				FileOutputStream fos = new FileOutputStream(new File(path));
				wb.write(fos);
				fos.close();
				wb.close();
				fileCreation= "successfull";
				System.out.println("Result File created");
			}
		} catch(IOException ioe) {
			ioe.printStackTrace();
			System.out.println("IO Exception during Result File Creation:  " + ioe.getMessage());
			fileCreation= "fail";
		}
		//return fileCreation;
	}
	public void writeResults(String path, String testCaseID,String testCaseName,String expectedResult,String result, String sheetName, String uniqueValue){
		try {
			System.out.println("In writeResults()"+path+":"+sheetName);
			int rowCnt = 0;
			FileInputStream fisWrite = new FileInputStream(new File(path));
			XSSFWorkbook wbWrite = new XSSFWorkbook(fisWrite);
			XSSFSheet shtWrite  = wbWrite.getSheet(sheetName);
			System.out.println("row Count" + shtWrite.getLastRowNum());
			rowCnt = shtWrite.getLastRowNum()+1;
			XSSFRow row = shtWrite.createRow(rowCnt);
			row.createCell(0).setCellValue(testCaseID);
			row.createCell(1).setCellValue(testCaseName);
			row.createCell(2).setCellValue(expectedResult);
			row.createCell(3).setCellValue(result);
			row.createCell(4).setCellValue(sheetName+":"+uniqueValue);
			fisWrite.close();
			FileOutputStream fosWrite = new FileOutputStream(new File(path));
			wbWrite.write(fosWrite);
			fosWrite.close();
			wbWrite.close();
		} catch(IOException ioe){
			ioe.printStackTrace();
			System.out.println("Failed to Read/Write Result File" + ioe.getMessage());
		}
		
		
	}
	
	public String getResultFromResultFile(String path, String workSheet){
		int rowCnt = 0;
		int pCnt = 0;
		int nExecCnt = 0;
		String fail = "False";
		String finalResult = "Fail";
		try {
			FileInputStream fisWrite = new FileInputStream(new File(path));
			XSSFWorkbook wbWrite = new XSSFWorkbook(fisWrite);
			XSSFSheet shtWrite  = wbWrite.getSheet(workSheet);
			rowCnt = shtWrite.getLastRowNum();
			for (int rc =1;rc<=rowCnt; rc++){
				System.out.println(shtWrite.getRow(rc).getCell(3).getStringCellValue());
				if(shtWrite.getRow(rc).getCell(3).getStringCellValue().equalsIgnoreCase("Pass")){
					pCnt = pCnt + 1;
				} else if (shtWrite.getRow(rc).getCell(3).getStringCellValue().equalsIgnoreCase("Fail")){
					fail = "True";
				} else {
					nExecCnt = nExecCnt + 1;
				}
			}
			System.out.println(pCnt + "  "+fail+ " "+nExecCnt);
			if (fail.equalsIgnoreCase("True")) {
				finalResult = "Fail"+";"+path;
			}else if (nExecCnt>0) {
				finalResult = "Pass but few test cases were not executed"+";"+path;
			}else {
				finalResult = "Pass"+";"+path;
			}
			fisWrite.close();
			wbWrite.close();
		} catch(IOException ioe){
			ioe.printStackTrace();
			System.out.println("Failed to Read/Write Result File" + ioe.getMessage());
		}
		return finalResult;
	}
	public String generateDateTime() {
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
		Date date = new Date();
		String dateTime = df.format(date).toString();
		return dateTime;
	}
	public String fileExists(String path){
		String fileStatus;
		File file = new File(path);
		if(file.exists()) {
			fileStatus = "exists";
		}else {
			fileStatus = "doen not exists";
		}
		return fileStatus;
	}
	public By generateObject(String locatorName){
		By by=null;
		try {
			String location = System.getProperty("user.dir")+"\\Test Data\\ObjectRepository.properties";
			FileInputStream fis = new FileInputStream(new File(location));
			Properties property = new Properties();
			property.load(fis);
			String locatorProperty = property.getProperty(locatorName);
			String locatorType = locatorProperty.split(":")[0];
			String locatorValue = locatorProperty.split(":")[1];
			switch(locatorType){
				case "Id":
					by = By.id(locatorValue);
					break;
				case "Name":
					by = By.name(locatorValue);
					break;
				case "CssSelector":
					by = By.cssSelector(locatorValue);
					break;
				case "LinkText":
					by = By.linkText(locatorValue);
					break;
				case "PartialLinkText":
					by = By.partialLinkText(locatorValue);
					break;
				case "TagName":
					by = By.tagName(locatorValue);
					break;
				case "Xpath":
					by = By.xpath(locatorValue);
					break;
			}
			
		} catch(IOException ioe){
			ioe.printStackTrace();
			System.out.println("Error in reading ObjectRepository.properties file: "+ioe.getMessage());
		}
		return by;
	}
	public void waitTime(int milliSeconds) {
		try {
			Thread.sleep(milliSeconds);
		}catch(InterruptedException i){
			System.out.println("Interrupted Exception" + i.getMessage());
		}
		
	}
	

}
