package com.abnamro.assignment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import resources.TestData;
import resources.Utils;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;

@Tag("UnitTest")
public class SampleUnitTest extends Utils {
	JsonPath js; 
	String IssueIidAct;
	String IssueTitleAct;
	String URI;
	Response res;
	String Title;
	String Label;
	String Description;
	TestData TD= new TestData();  // This class will build the Test data.
	
	@BeforeEach  //This method will create an Issue before each test case, just to remove dependency(each test case will run individually) 
	@DisplayName("Create an Issue")
	public void createIssue() throws IOException {
    	 Title = "Payment getting fail";
    	 Label ="bug";
    	 Description ="User is unable to make payments";
    	// Above variable's values will be fetch from test data sheet. Please let me know if it is required for this assignment.
    	    	   
    	res = given().log().all().spec(requestSpecification()).pathParam("ProjID",getGlobalValue("PorjectID")) //ProjectID from a property file
        		              .header("Content-Type","application/json")
        		              .body(TD.addCreateIssuePayload(Title, Label,Description)) //Used json payload instead of parameters
        		      .when().post("/{ProjID}/issues")
                      .then().log().all().assertThat().statusCode(201).extract().response();
    	
        IssueIidAct= getJsonPathValue(res,"iid");   // get Issue ID from response.        
        IssueTitleAct= getJsonPathValue(res,"title"); // get Issue Title from response
        
        
	}

	
	
    @Test
    @DisplayName("TC1: Verify that correct Issue has been created")
    public void verifyCreatedIssue() throws IOException {
    			
    	assertAll("strings",
                () -> assertEquals(Title,IssueTitleAct),                 			 //Validate the expected title and actual title of an Issue
                () -> assertEquals(Description,getJsonPathValue(res,"description")), //Validate the expected description and actual description of an Issue
                () -> assertEquals(getGlobalValue("PorjectID"),getJsonPathValue(res,"project_id")),//Validate that the issue is created within same Project
                () -> assertEquals( getJsonPathValue(res,"state"),"opened")               		  //Verify that status of issue should be 'opened'. //this value must be a variable from test data
    			);
    	deleteAnIssue();
    }
    
    @Test
    @DisplayName("TC2: Edit an Issue")
    public void updateAnIssue() throws IOException {
  //Change the Issue status from opened to closed by PUT method.
        String changeIssueStatusTo = "close";
        Response getIssueUpdate = given().log().all().spec(requestSpecification()).pathParam("ProjID",getGlobalValue("PorjectID")).queryParam("state_event", changeIssueStatusTo)
        .when().put("{ProjID}/issues/"+IssueIidAct+"")
        .then().log().all().assertThat().statusCode(200).extract().response();
       
       String IssueStatus =getJsonPathValue(getIssueUpdate,"state");
       assertEquals(IssueStatus, "closed");   //Verify that status of Issue has been changed to Closed.//this value must be a variable from test data
       deleteAnIssue();
    }
    
    @Test
    @DisplayName("TC1: Get an Issue")
    public void getIssue() throws IOException {  
        //Just to validate GET method.
        
    	res = given().log().all().spec(requestSpecification()).pathParam("ProjID", getGlobalValue("PorjectID"))
        .when().get("{ProjID}/issues/"+IssueIidAct+"")
        .then().assertThat().statusCode(200).extract().response();   // Validate that Issue is getting fetched with status code 200.
    	
    	
    	// verify response with correct title and projectID
    	assertAll("strings",
                () -> assertEquals(Title,getJsonPathValue(res,"title")),               			 
                () -> assertEquals(getGlobalValue("PorjectID"),getJsonPathValue(res,"project_id"))   
            );
          
         deleteAnIssue();
    }
    
    @Test
    @DisplayName("TC1: Delete an Issue")
    public void deleteAnIssue() throws IOException {
       
        String deleteResponse = given().log().all().spec(requestSpecification()).pathParam("ProjID", getGlobalValue("PorjectID"))
        .when().delete("/{ProjID}/issues/"+IssueIidAct+"")
        .then().log().all().assertThat().statusCode(204).extract().response().asString(); //verify status code is 204
        boolean flag = deleteResponse.isEmpty();      //Verify that response is Empty
        assertTrue(flag); 
   
    }
}
