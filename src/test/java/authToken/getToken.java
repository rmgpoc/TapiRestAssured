package authToken;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class getToken {
	
	public static Map<String, String> getAuthToken(){
		
		RestAssured.baseURI="https://pp.api.royalmail.net";
		Response res = given().
			relaxedHTTPSValidation().
			log().
			all().
			header("X-IBM-Client-Id", "a69b2209-5a5e-49cf-b912-666d3199d57e").
			header("X-IBM-Client-Secret", "K3lT6uJ7eU3cU3rM4sH2gQ4pA0lP3aM6eQ3eW2mU3lR1iR4sY3").
			header("origin", "alertsite.com").
			queryParam("userId", "apimonitor").
		when().
			get("/authorisation/token").
		then().
			log().
			all().
			statusCode(200).
			and().
			assertThat().contentType(ContentType.JSON).
			extract().response();
		
		JsonPath js = new JsonPath(res.asString());
		Map<String, String> auth = new HashMap<String, String>();
		auth.put("rmgAuthToken", js.get("rmgAuthToken").toString());
		auth.put("recaptchaSession", js.get("recaptchaSession").toString());
		auth.put("userId", js.get("userId").toString());
		return auth;
	}
	
	public static String getBrowserHistory(){
		
		Map<String, String> authToken = getToken.getAuthToken();
		Response res = given().
				relaxedHTTPSValidation().
				log().
				all().
				header("X-IBM-Client-Id", "a69b2209-5a5e-49cf-b912-666d3199d57e").
				header("X-RMG-AUTH-TOKEN", authToken.get("rmgAuthToken")).
				header("origin", "royalmail.com").
				pathParam("userId", authToken.get("userId")).
				pathParam("siteId", "a69b2209-5a5e-49cf-b912-666d3199d57e").
				queryParam("limit", "30").
			when().
			get("/mailpieces/v2/user/{userId}/history/{siteId}").
			then().
				log().
				all().
				statusCode(200).
				and().
				assertThat().contentType(ContentType.JSON).
				extract().response();

		JsonPath js =new JsonPath(res.asString());
		String jwt = js.get("jwt");	
		return jwt;
		
	}

}
