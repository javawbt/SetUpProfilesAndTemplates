package a3Scripts;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
/*
 1. Create Credentials - Create Credentials
 2. Create Cloud Account
 3. Setup Location and credentials for LDCs - Setup Location and credentials for LDCs
 4. Set Limits to LDC resources
 5. Discover
 6. Setup profiles and templates
 */

public class OnboardCloudAccount {

	HttpClient client;
	HttpPost httpPost;
	HttpPut httpPut;
	HttpResponse response;
	HttpGet httpGet;
	HttpEntity httpEntity;
	JSONParser parser;
	InputStream inputStream;
	Object obj;
	JSONObject jsonObject;
	String actualStatus;
	String actualErrorCode;
	String errorMessage;
	String success;
	String failure;
	String filePath = "E:/LogFile.txt";
	String thisMethodName;
	@Before
	public void beforeEachIdividualTest() throws ClientProtocolException, IOException {
		thisMethodName = "beforeEachIdividualTest";
		client = HttpClients.createDefault();

		httpPost = new HttpPost("http://10.10.8.32:8080/AppCenter/services/rest/login");
		String json = "{\"username\":\"XXXXXX\",\"password\":\"XXXXXX\"}";
		StringEntity entity = new StringEntity(json);
		httpPost.setEntity(entity);
		httpPost.setHeader("Content-type", "application/json");
		response = client.execute(httpPost);
	}

	@After
	public void afterEachIdividualTest() throws ClientProtocolException, IOException, org.json.simple.parser.ParseException {
		httpEntity = response.getEntity();
		InputStream is = httpEntity.getContent();
		String logFilePath = "E:/LogFile.txt";
		FileOutputStream fos = new FileOutputStream(new File(logFilePath));
		int inByte;
		while ((inByte = is.read()) != -1)
			fos.write(inByte);
		// is.close();
		// fos.close();

		parser = new JSONParser();

		obj = parser.parse(new FileReader(logFilePath));

		jsonObject = (JSONObject) obj;

		actualStatus = (String) jsonObject.get("status");
		actualErrorCode = (String) jsonObject.get("errorCode");
		errorMessage = (String) jsonObject.get("errorMessage");
		// String data = (String) jsonObject.get("data");
		success = "success";
		failure = "failure";
		System.out.println("\t\t\t" + thisMethodName + "");
		System.out.println("Status:		" + actualStatus);
		System.out.println("Error Code:	" + actualErrorCode);

		assertEquals(success, actualStatus);
		// Assert.assertTrue("status".equals("success"));
		// Assert.assertTrue(success.equals(actualStatus));
	}
	@Test
	public void LoginAndEstablishSession1() throws ClientProtocolException, IOException {
		thisMethodName = "LoginAndEstablishSession4";
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("http://10.10.8.32:8080/AppCenter/services/rest/login");
		String json = "{\"username\":\"XXXXXX\",\"password\":\"XXXXXX\"}";
		StringEntity entity = new StringEntity(json);
		httpPost.setEntity(entity);
		httpPost.setHeader("Content-type", "application/json");
		CloseableHttpResponse response = client.execute(httpPost);
		assertEquals(200, response.getStatusLine().getStatusCode());
		System.out.println("Response for LoginAndEstablishSession4 : 	" + response);
	}

	@Test
	// if the cloud accont already exists the test will not pass
	public void CreateCloudAccountImportXML2() throws ClientProtocolException, IOException {
		thisMethodName = "CreateCloudAccountImportXML";
		httpPost = new HttpPost("http://10.10.8.32:8080/AppCenter/services/rest/cloudaccount/import");
		File file2 = new File("E:/Downloads/XenMaxTestCloudAccountForCredentials1.xml");
		MultipartEntity mpEntity2 = new MultipartEntity();
		ContentBody cbFile2 = new FileBody(file2);
		mpEntity2.addPart("ca", cbFile2);
		httpPost.setEntity(mpEntity2);
		response = client.execute(httpPost);
		System.out.println("Response for CreateCloudAccountImportXML : 	" + response);

	}

	@Test
	public void SetLimitsToLDCResources4() throws ClientProtocolException, IOException {
		thisMethodName = "SetLimitsToLDCResources4";
		httpPost = new HttpPost("http://10.10.8.32:8080/AppCenter/services/rest/cloudaccount/capacity" + "?cloudAccount=OpenStack%20Cloud%20Account&cloudLocation=RegionOne&resource=Virtual%20Server&capacity=22");
		response = client.execute(httpPost);
	}
	@Test
	public void GetDiscoverAll() throws ClientProtocolException, IOException, org.json.simple.parser.ParseException {
		thisMethodName = "GetDiscoverAll";
		httpGet = new HttpGet("http://10.10.8.32:8080/AppCenter/services/rest/cloudaccount/OpenStack%20Cloud%20Account/discoverAll");
		response = client.execute(httpGet);
		 
	}
	
	@Test
	public void ExportProfile6() throws ClientProtocolException, IOException, ParseException {
		thisMethodName = "ExportProfile";

		
		httpPost = new HttpPost("http://10.10.8.32:8080/AppCenter/services/rest/standards/fetch");
		String json = "{\"Name\":\"OS Profile\", "
		 		+ "\"Criteria\": {\"ProfileID\":\"UBUNTU\"}}";
		StringEntity entity = new StringEntity(json);
		httpPost.setEntity(entity);
		httpPost.setHeader("Content-type", "application/json");
		 response = client.execute(httpPost);
		 
		 httpEntity = response.getEntity();
			InputStream is = entity.getContent();
			String logFilePath = "E:/UBUNTUExportedProfile.txt";
			FileOutputStream fos = new FileOutputStream(new File(logFilePath));
			int inByte;
			while ((inByte = is.read()) != -1)
				fos.write(inByte);
			// is.close();
			// fos.close();

			parser = new JSONParser();

			obj = parser.parse(new FileReader(logFilePath));

			jsonObject = (JSONObject) obj;

			actualStatus = (String) jsonObject.get("status");
			actualErrorCode = (String) jsonObject.get("errorCode");
			errorMessage = (String) jsonObject.get("errorMessage");
			// String data = (String) jsonObject.get("data");
			success = "success";
			failure = "failure";
			System.out.println("\t\t\t" + thisMethodName + "");
			System.out.println("Status:		" + actualStatus);
			System.out.println("Error Code:	" + actualErrorCode);

			assertEquals(success, actualStatus);
	}
	@Test
	public void ExportProfileFluentAPI() throws ClientProtocolException, IOException {

		
		Executor executor = Executor.newInstance();
		
		System.out.println(executor.execute(Request.Post("http://10.10.8.32:8080/AppCenter/services/rest/login")
					 .useExpectContinue()
					 .version(HttpVersion.HTTP_1_1)
					 .bodyString("{\"username\":\"admin\",\"password\":\"admin\"}", ContentType.APPLICATION_JSON)).returnResponse().toString());
				
				
		executor.execute(Request.Post("http://10.10.8.32:8080/AppCenter/services/rest/standards/fetch")
				 .useExpectContinue()
				  .version(HttpVersion.HTTP_1_1)
				 .bodyString("{\"Name\":\"OS Profile\", "
				 		+ "\"Criteria\": {\"ProfileID\":\"UBUNTU\"}}", ContentType.APPLICATION_JSON))
				 .saveContent(new File("E:/newfile1UBUNTU.txt"));
		
}
	



}
