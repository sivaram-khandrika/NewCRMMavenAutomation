package CRMAutomation.CRMAutomation;


import java.io.IOException;

//import crmGenericMethods.GlobalMethods;
//import  crmModulesFunctionality.*;

public class CRMModuleLoad {
	public String finalResultStatus = "";
	public String loadModule(GlobalMethods gm,String filePath,String moduleName,String subModuleName) throws IOException {
		System.out.println(" In loadModule()");
		if(moduleName.equalsIgnoreCase("Accounts")){
			finalResultStatus = loadAccountsSubModule(gm,filePath,moduleName,subModuleName);
		} else if(moduleName.equalsIgnoreCase("Contacts")) {
			finalResultStatus = loadContactsSubModule(gm, filePath, moduleName, subModuleName);
		}
		System.out.println("Final Result from loadModule()"+finalResultStatus);
		return finalResultStatus;
	}
	public String loadAccountsSubModule(GlobalMethods gm,String filePath,String moduleName,String subModuleName) throws IOException {
		System.out.println(" In loadSUbModule()");
		if((subModuleName.isEmpty()) && (moduleName.equalsIgnoreCase("Accounts"))){
			
			CRMAccountsModule crmAcc = new CRMAccountsModule();
			finalResultStatus= crmAcc.crmAccExecute(gm,filePath,moduleName,subModuleName);
			
		} else if((subModuleName.isEmpty()) && (moduleName.equalsIgnoreCase("Contacts"))) {
			
		}
		System.out.println("Final Result from loadSUbModule()"+finalResultStatus);
		return finalResultStatus;
	}
	public String loadContactsSubModule(GlobalMethods gm,String filePath,String moduleName,String subModuleName) throws IOException {
		System.out.println(" In loadContactsSubModule()");
		if((subModuleName.isEmpty()) && (moduleName.equalsIgnoreCase("Contacts"))){
			
			CRMContactsModule crmCon = new CRMContactsModule();
			finalResultStatus= crmCon.crmContactExecute(gm,filePath,moduleName,subModuleName);			
		} 
		System.out.println("Final Result from loadContactsSUbModule()"+finalResultStatus);
		return finalResultStatus;
	}

}
