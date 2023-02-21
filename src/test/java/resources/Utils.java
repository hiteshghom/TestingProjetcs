package resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Utils {
	public  RequestSpecification req;
	
	//This method will return jsonPath value from response.
	public String getJsonPathValue(Response response,String key)
	{
		  String resp=response.asString();
		JsonPath   js = new JsonPath(resp);
		return js.get(key).toString();
	}
	
	// Read property file and return its value
	public static String getGlobalValue(String key) throws IOException
	{
		Properties prop =new Properties();
		
		FileInputStream fis =new FileInputStream("./src/test/java/resources/global.properties");
		prop.load(fis);
		return prop.getProperty(key);
			
		
	}
	
	//Request Spec builder
	public RequestSpecification requestSpecification() throws IOException
	{	 
		req=new RequestSpecBuilder().setBaseUri(getGlobalValue("baseUrl")).addHeader("PRIVATE-TOKEN",getGlobalValue("Token")).build();
		//read token and baseUrl from property file
		 return req;

	}

}
