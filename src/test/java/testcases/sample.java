package testcases;

import static io.restassured.RestAssured.given;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import authToken.getToken;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class sample {
	
	@Test
	public void getServerEvents(){
		
		RestAssured.baseURI="https://pp.api.royalmail.net";
		given().relaxedHTTPSValidation().
			log().
			all().
			header("Accept", "application/json").
			header("Content-Type", "application/json").
			header("X-IBM-Client-Id", "09650c29-3551-4e40-9663-4a0c7831e5be").
			header("X-IBM-Client-Secret", "M2qI2iA4rK1oV0aO8cI5cT1bP4oH2dL5dC7lB5yB0eW6kD2kO8").
			header("X-RMG-Language", "en").
			pathParam("MailpieceId", "BB292838062GB").
		when().
			get("/mailpieces/v2/{MailpieceId}/events").
		then().
			log().
			all().
			statusCode(200).
			and().
			assertThat().contentType(ContentType.JSON).
			extract().response();
		
	}
	
	@Test
	public void getBrowserEvents(){
		
		Map<String, String> authToken = getToken.getAuthToken();
		Response res = given().
				relaxedHTTPSValidation().
				log().
				all().
				header("X-IBM-Client-Id", "a69b2209-5a5e-49cf-b912-666d3199d57e").
				header("X-RMG-API-SESSION", authToken.get("recaptchaSession")).
				header("X-RMG-AUTH-TOKEN", authToken.get("rmgAuthToken")).
				header("origin", "royalmail.com").
				header("X-RMG-LANGUAGE", "en").
				pathParam("MailpieceId", "BB292838062GB").
			when().
			get("/mailpieces/v2/{MailpieceId}/events").
			then().
				log().
				all().
				statusCode(200).
				and().
				assertThat().contentType(ContentType.JSON).
				extract().response();

		JsonPath js =new JsonPath(res.asString());
		Assert.assertEquals(js.get("mailPieces.mailPieceId"), "BB292838062GB");		
		
	}
	
	@Test
	public void getBrowserSummary(){
		
		Map<String, String> authToken = getToken.getAuthToken();
		given().
				relaxedHTTPSValidation().
				log().
				all().
				header("X-IBM-Client-Id", "a69b2209-5a5e-49cf-b912-666d3199d57e").
				header("X-RMG-AUTH-TOKEN", authToken.get("rmgAuthToken")).
				header("origin", "royalmail.com").
				header("X-RMG-LANGUAGE", "en").
				queryParam("mailPieceId", getToken.getBrowserHistory()).
			when().
			get("/mailpieces/v2/summary").
			then().
				log().
				all().
				statusCode(200).
				and().
				assertThat().contentType(ContentType.JSON);
		
	}

}
