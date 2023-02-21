package resources;

import pojo.CreateIssue;

public class TestData {
	
	 //Pass title and lable value from external/test data sheet. 
	//Note: I am not creating test data sheet or database to store test data as a part of this assignment, it will take a time.
	// It is not recommended to pass data directly in scripts.
	
	public CreateIssue addCreateIssuePayload(String title, String label,String description) {
		CreateIssue CreateIssuePayloadObj = new CreateIssue();
		CreateIssuePayloadObj.setTitle(title);
		CreateIssuePayloadObj.setLabel(label);
		CreateIssuePayloadObj.setDescription(description);
		return CreateIssuePayloadObj;

	}

}
