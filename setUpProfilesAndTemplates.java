package a3Scripts;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

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
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class setUpProfilesAndTemplates {
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
	HttpEntity resEntity;
	Random r = new Random();

	String alphabet = "XxYyZzOoIiQqWwEeRrTtYyUuAaSsDdFfGg";

	char randomAlphabet = alphabet.charAt(r.nextInt(alphabet.length()));

	@Before
	public void beforeEachIdividualTest() throws ClientProtocolException, IOException {
		thisMethodName = "beforeEachIdividualTest";
		client = HttpClients.createDefault();

		httpPost = new HttpPost("http://10.10.8.32:8080/AppCenter/services/rest/login");
		String json = "{\"username\":\"xxxxx\",\"password\":\"xxxxxx\"}";
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
	public void ExportDownloadProfiles() throws ClientProtocolException, IOException {

		Executor executor = Executor.newInstance();

		System.out.println(executor.execute(Request.Post("http://10.10.8.32:8080/AppCenter/services/rest"
				+ "/login").useExpectContinue().version(HttpVersion.HTTP_1_1)
				.bodyString("{\"username\":\"xxxxx\","
						+ "\"password\":\"xxxx\"}", ContentType.APPLICATION_JSON))
						.returnResponse().toString());

		executor.execute(Request.Post("http://10.10.8.32:8080/AppCenter/services/rest/standards/fetch")
				.useExpectContinue()
				.version(HttpVersion.HTTP_1_1)
				.bodyString("{\"Name\":\"OS Profile\", " 
				+ "\"Criteria\": {\"ProfileID\":\"UBUNTU\"}}", ContentType.APPLICATION_JSON))
				.saveContent(new File("E:/newfile1.txt"));

	}
	@Test
	public void ImportUploadProfiles() throws ClientProtocolException, IOException {
		httpPost = new HttpPost("http://10.10.8.32:8080/AppCenter/services/rest/standards?name=OS+Profile");
		File file = new File("E:/Downloads/UBUNTU.json");
		FileEntity entityFile = new FileEntity(file, ContentType.create("binary/octet-stream", "UTF-8"));

		entityFile.setChunked(true);

		httpPost.setEntity(entityFile);

		MultipartEntity mpEntity = new MultipartEntity();
		ContentBody cbFile = new FileBody(file, "application/octet-stream");
		mpEntity.addPart("UBUNTU.json", cbFile);
		httpPost.setEntity(mpEntity);
		System.out.println("executing request " + httpPost.getRequestLine());
		response = client.execute(httpPost);
		resEntity = response.getEntity();

		System.out.println(response.getStatusLine());
		if (resEntity != null) {
			System.out.println(EntityUtils.toString(resEntity));
		}
		if (resEntity != null) {
			resEntity.consumeContent();
		}

	}

}
