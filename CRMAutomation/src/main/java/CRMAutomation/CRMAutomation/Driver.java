package CRMAutomation.CRMAutomation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.openqa.selenium.WebDriver;
//import crmModules.*;
//import crmGenericMethods.*;


public class Driver {
	//public String browserToInvoke;
	//public String url = "https://oakgtsci.crm8.dynamics.com/";
	public String executeModule;
	public String moduleName;
	public String subModuleName;
	public String status;
	public String resultFileName;
	public int rowCount;
	public String finaleResult;
	public String sheetName;
	public String status1;
	FileInputStream fis;
	XSSFWorkbook wb;
	XSSFSheet sheet ;
	boolean isException = false;
	int exceptionCount =1;
	int rc;
	String location;
	GlobalMethods gm = new GlobalMethods();
	//DriverClass dc = new DriverClass();

	//XSSFSheet sheet = null;
	public void driverLoadModules() throws IOException {
		
		try {
			System.out.println("Location is "+location);
			location = System.getProperty("user.dir")+"\\Test Data\\Driver.xlsx";
			System.out.println("Location is "+location);
			fis = new FileInputStream(new File(location));
			wb = new XSSFWorkbook(fis);
			sheet = wb.getSheet("Driver");
			//XSSFRow row;
			
			rowCount = sheet.getLastRowNum();
			for (rc = exceptionCount; rc<= rowCount;rc++){
				executeModule = sheet.getRow(rc).getCell(0).getStringCellValue();
				moduleName = sheet.getRow(rc).getCell(1).getStringCellValue();
				subModuleName = sheet.getRow(rc).getCell(2).getStringCellValue();
				if(executeModule.equalsIgnoreCase("yes")){
					/*String rFileName; 
					if (subModuleName.isEmpty()){
						rFileName = moduleName;
					} else {
						rFileName = moduleName+"_"+subModuleName;
					}
					sheetName = rFileName;*/
					//status = "Fail";
					//System.out.println("Result Folder is Created" +sheetName);
					String filePath=	gm.createResultFolder();
					//resultFileName = filePath + "\\" +rFileName+"_" +gm.generateDateTime() +".xlsx";
					//System.out.println("Result Folder is Created");
					CRMModuleLoad crmModuleLoad = new CRMModuleLoad();
					
					status1 = crmModuleLoad.loadModule(gm, filePath,moduleName, subModuleName);
					
					//System.out.println("Final Status in Driver" + status + ":"+resultFileName);
					
					//System.out.println("Files Status in Driver" +status);
					status = status1.split(";")[0];
					resultFileName = status1.split(";")[1];
						if( status1.matches("File Deos not exists*.*")){
						status = status1.split(";")[0];
						resultFileName = status1.split(";")[1];
					}else {
						status = status1.split(";")[0];
						resultFileName = status1.split(";")[1];
					}
					sheet.getRow(rc).getCell(3).setCellValue(status);
					sheet.getRow(rc).getCell(4).setCellValue(resultFileName);

				}
				//End of the each row.. update the result fetched from the loaded module
				System.out.println("-------------------------"+rc+"-----------------------------------");
			}
			fis.close();
			FileOutputStream fos = new FileOutputStream(new File(location));
			wb.write(fos);
			fos.close();
			wb.close();
			System.out.println("Done");
		} catch (IOException ioe) {
			fis.close();
			FileOutputStream fos = new FileOutputStream(new File(location));
			wb.write(fos);
			fos.close();
			wb.close();
			isException = true;
			System.out.println("Error in Driver.xlsx File ");
			ioe.printStackTrace();
			System.out.println("Error message from Exception is :" +ioe.getMessage());
		} finally {
			if(isException == true){
				exceptionCount = rc+1;
				isException = false;
				driverLoadModules();
			}
		}
	}
	
	public static void main(String[] args) {
		Driver dc = new Driver();
		try {
			dc.driverLoadModules();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("You are in Catch Block of main()");
			System.out.println(e.getMessage());
		}
	}

}
