package org.petstore;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PetStore {
	public int id;
	@DataProvider(name="status")
	public Object[][] getstatus(){
		return new Object[][] {{"sold","available","pending"}};
	}
	
	@Test(priority=1)
	public void post() {
		System.out.println("start posting");
		RestAssured.baseURI="https://petstore.swagger.io/v2";
		String response=given().header("Content-Type","application/json")
		.body("{\r\n" + 
				"  \"id\": 146,\r\n" + 
				"  \"category\": {\r\n" + 
				"    \"id\": 99,\r\n" + 
				"    \"name\": \"dog\"\r\n" + 
				"  },\r\n" + 
				"  \"name\": \"lion\",\r\n" + 
				"  \"photoUrls\": [\r\n" + 
				"    \"string\"\r\n" + 
				"  ],\r\n" + 
				"  \"tags\": [\r\n" + 
				"    {\r\n" + 
				"      \"id\": 33,\r\n" + 
				"      \"name\": \"german\"\r\n" + 
				"    }\r\n" + 
				"  ],\r\n" + 
				"  \"status\": \"available\"\r\n" + 
				"}")
		.when().post("/pet")
		.then().log().all().assertThat().statusCode(200).body("id", equalTo(146))
		.extract().response().asString();
		System.out.println(response);
		
		JsonPath js= new JsonPath(response);
		id = js.get("id");
		System.out.println(id);
		int id1 = js.get("category.id");
		System.out.println(id1);
		int id2 = js.get("tags[0].id");
		System.out.println(id2);
		
	
	}
	@Test(priority=2)
	public void Get() {
		String getresponse=given().log().all().pathParam("id", id).header("Content-Type","application/json")
		.when().get("/pet/{id}").then().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js1= new JsonPath(getresponse);
		int gid = js1.get("id");
		System.out.println(gid);
		
		Assert.assertEquals(id,gid);
		
		 
		
	}
	@Test(priority=3)
	public void FiindByStatus(String status) {
		String qres=given().log().all().queryParam("status","sold").header("Content-Type","application/json")
		.when().get("/pet/findByStatus").then().assertThat().statusCode(200).extract().response().asString();
		JsonPath js2= new JsonPath(qres);
		System.out.println(qres);
	}

}
