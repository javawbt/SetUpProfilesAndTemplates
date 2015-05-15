package a3Scripts;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
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

public class Governance {
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
		 
	}

	@Test
	public void SetupPermissionsObjects() throws ClientProtocolException, IOException {
		thisMethodName = "SetupPermissionsObjects";
		httpPost = new HttpPost("http://10.10.8.32:8080/AppCenter/services/rest/permission/assign?entity=Blueprints&instanceName=RMTest&isGroup=true&accessorName=PermGroup");
		String json = "{\"accessPermission\":\"true\",\"managePermission\":\"false\"}";
		StringEntity entity = new StringEntity(json);
		httpPost.setEntity(entity);
		httpPost.setHeader("Content-type", "application/json");
		 response = client.execute(httpPost);
	
	}
	
	@Test
	public void SetUpPermissionOnResourceInstances() throws ClientProtocolException, IOException, ParseException {
		thisMethodName = "SetUpPermissionOnResourceInstances";

		
		httpPost = new HttpPost("http://10.10.8.32:8080/AppCenter/services/rest/permission/assign?entity=Resource+Model&instanceName=Virtual+Server&resourceLookUp=RegionOne%2F0ed7c6a4-ad34-4e40-9abb-6af70e3f6e59&isGroup=true&accessorName=PermGroup");
		String json = "{\"accessPermission\":\"true\",\"managePermission\":\"false\"}";
		StringEntity entity = new StringEntity(json);
		httpPost.setEntity(entity);
		httpPost.setHeader("Content-type", "application/json");
		 response = client.execute(httpPost);
	
	}
	
	@Test
	public void SetupQuota() throws ClientProtocolException, IOException, ParseException {
		thisMethodName = "SetupQuota";

		
		httpPost = new HttpPost("http://10.10.8.32:8080/AppCenter/services/rest/quota/assign?cloudAccount=OpenStack%20Cloud%20Account&cloudLocation=RegionOne&group=Global%20Admin%20Group&quota=VM_Count");
		String json = "{\"maxLimit\": 5,\"perUserLimit\":3}";
		StringEntity entity = new StringEntity(json);
		httpPost.setEntity(entity);
		httpPost.setHeader("Content-type", "application/json");
		 response = client.execute(httpPost);
	
	}
	@Test
	public void ImportPolicyFromXML () throws ClientProtocolException, IOException, ParseException {
		thisMethodName = "ImportPolicyFromXML";
 
		 
		httpPost = new HttpPost("http://10.10.8.32:8080/AppCenter/services/rest/policy/import?override=false");
		File file2 = new File("E:/Downloads/DefaultApprovalPolicy.xml");
		MultipartEntity mpEntity2 = new MultipartEntity();
		ContentBody cbFile2 = new FileBody(file2);
		mpEntity2.addPart("policy", cbFile2);
		httpPost.setEntity(mpEntity2);
		response = client.execute(httpPost);
		System.out.println("Response for CreateCloudAccountImportXML : 	" + response);
	
	}
	@Test
	public void SetUpPublishPolicy() throws ClientProtocolException, IOException, ParseException {
		thisMethodName = "SetUpPublishPolicy";

		
		httpPost = new HttpPost("http://10.10.8.32:8080/AppCenter/services/rest/policy/Default+Claim+Policy/publish");
	
		 response = client.execute(httpPost);
		 
	
	}
}
