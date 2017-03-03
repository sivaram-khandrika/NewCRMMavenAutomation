package CRMAutomation.CRMAutomation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

//import crmGenericMethods.GlobalMethods;

public class CRMAccountsModule {
	
	String testCaseID;
	String testCaseName;
	String loginName;
	String password;
	String accountName;
	String primaryContact;
	String expectedResult;
	String execute;
	String finalResult = "Fail";
	String result;
	int rowCount =0;
	public String sheetName;
	public String resultFileName,resultFileNameDuplicate;
	XSSFWorkbook wbAcc ;
	XSSFSheet shtAcc ;
	FileInputStream fisAcc;
	FileOutputStream fosAcc;
	boolean isException = false;
	int exceptionCount = 1;
	int mRC;
	WebDriver driver;
	
	String readFile;
	//@SuppressWarnings("finally")
	public String crmAccExecute(GlobalMethods gm,String filePath,String moduleName,String subModuleName) throws IOException {
		String location = System.getProperty("user.dir");
		readFile = location+"\\Test Data\\"+moduleName+".xlsx";
		if(gm.fileExists(readFile).equalsIgnoreCase("exists")){
			try {
				String rFileName; 
				if (subModuleName.isEmpty()){
					rFileName = moduleName;
					sheetName = moduleName;
				} else {
					rFileName = moduleName+"_"+subModuleName;
					sheetName = subModuleName;
				}
				//sheetName = rFileName;
				if(isException == true) {
					System.out.println("In True");
					resultFileName = resultFileNameDuplicate;
					isException = false;
				}else {
					System.out.println("In False");
					resultFileName = filePath + "\\" +rFileName+"_" +gm.generateDateTime() +".xlsx";
					resultFileNameDuplicate = resultFileName;
					System.out.println("crmAccExecute()"+sheetName);
					gm.createResultFile(resultFileName, sheetName);
				}

				fisAcc = new FileInputStream(new File(readFile));
				wbAcc = new XSSFWorkbook(fisAcc);
				shtAcc = wbAcc.getSheet(sheetName);
				rowCount = shtAcc.getLastRowNum();
				System.out.println("Row Count"+rowCount);
				for (mRC=exceptionCount;mRC<=rowCount;mRC++){
					
					testCaseID = shtAcc.getRow(mRC).getCell(0).getStringCellValue();
					testCaseName= shtAcc.getRow(mRC).getCell(1).getStringCellValue();
					execute= shtAcc.getRow(mRC).getCell(2).getStringCellValue();
					loginName = shtAcc.getRow(mRC).getCell(8).getStringCellValue();
					password = shtAcc.getRow(mRC).getCell(9).getStringCellValue();
					String uniqueNumber =gm.generateDateTime();
					uniqueNumber = uniqueNumber.replaceAll("-", "");
					System.out.println(uniqueNumber);
					accountName= shtAcc.getRow(mRC).getCell(3).getStringCellValue()+uniqueNumber;
					primaryContact= shtAcc.getRow(mRC).getCell(4).getStringCellValue();
					expectedResult= shtAcc.getRow(mRC).getCell(5).getStringCellValue();
					if (execute.equalsIgnoreCase("Yes")){
						//Code to execute Accounts creation
						result = createAccount(gm,filePath,moduleName,subModuleName);
						//Code to execute Accounts creation
						//if condition is only for evaluating the results
						shtAcc.getRow(mRC).getCell(6).setCellValue(accountName);
						shtAcc.getRow(mRC).getCell(7).setCellValue(result);
					} else {
						result = "Not Executed";
						shtAcc.getRow(mRC).getCell(7).setCellValue("Not Executed");
					}
					System.out.println("Calling writeResults()");
					gm.writeResults(resultFileName, testCaseID,testCaseName,expectedResult,result,sheetName,accountName);
				}
				fisAcc.close();
				fosAcc = new FileOutputStream(new File(readFile));
				wbAcc.write(fosAcc);
				fosAcc.close();
				wbAcc.close();
				//finalResult = gm.getResultFromResultFile(resultFileName, sheetName);
				//finalResult = finalResult+":"+resultFileName;
				//return finalResult;
			} catch (Exception ioe){
				fisAcc.close();
				//System.out.println("FileName is" + readFile);
				result = "Fail";
				shtAcc.getRow(mRC).getCell(6).setCellValue(accountName);
				shtAcc.getRow(mRC).getCell(7).setCellValue(result);
				fosAcc = new FileOutputStream(new File(readFile));
				
				wbAcc.write(fosAcc);
				fosAcc.close();
				wbAcc.close();
				gm.writeResults(resultFileName, testCaseID,testCaseName,expectedResult,result,sheetName,accountName);
				isException = true;
				ioe.printStackTrace();
				System.out.println("Error Message from Accounts Module File Reading and the message is - " +ioe.getMessage());
				//return finalResult;
			} finally {
				if(isException == true){
					exceptionCount = mRC+1;
					//isException = false;
					crmAccExecute(gm,filePath, moduleName,subModuleName);
				}
				
			} 
			System.out.println("Final Result from crmAccExecute() " +finalResult);
			finalResult = gm.getResultFromResultFile(resultFileName, sheetName);
			return finalResult;
		} else {
			String fResult = "File Deos not exists.  Please Check;No Result File";
			return fResult;
		}
	}
	
	public String createAccount(GlobalMethods gm,String filePath,String moduleName,String subModuleName){
		driver = gm.createWebDriver("Chrome");
		gm.navigateURL(driver, "https://okncrm2.crm8.dynamics.com");
		System.out.println("Row Count in createAccount() is "+mRC); 
		if (mRC ==1) {
			driver.findElement(By.id("cred_userid_inputtex")).sendKeys(loginName);
		}else{
			driver.findElement(gm.generateObject("userName")).sendKeys(loginName);
		}
		gm.waitTime(2000);
		driver.findElement(gm.generateObject("userName")).sendKeys(Keys.TAB);;
		driver.findElement(gm.generateObject("password")).sendKeys(password);
		gm.waitTime(5000);
		driver.findElement(gm.generateObject("signIn")).click();
		result = "Pass";
		driver.close();
		return result;
	}
	
}
