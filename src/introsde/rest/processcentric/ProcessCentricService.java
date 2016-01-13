package introsde.rest.processcentric;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.glassfish.jersey.client.ClientConfig;
import org.json.*;

import introsde.model.rest.Goal;
import introsde.model.rest.LifeStatus;




@Stateless // will work only inside a Java EE application
@LocalBean // will work only inside a Java EE application
@Path("/centricprocess")
public class ProcessCentricService {

	@PUT
    @Path("/setVerifyLifeStatus/{idPerson}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response verifyLifeStatus(@PathParam("idPerson") int idPerson, LifeStatus lf) throws IOException {

		//PUT LIFESTATUS
    	String ENDPOINT = "https://morning-lake-2272.herokuapp.com/storeservice/updateProfile/"+idPerson;
    	ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		
		WebTarget service = client.target(ENDPOINT);

    	Response res = null;
		String putResp = null;
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//get current date time with Date()
		Date date = new Date();
		String currentDate = dateFormat.format(date);
		lf.setDateRegistered(currentDate);

		
    	String updateLifeStatus ="{"
        			+ "\"measureValue\": \""+lf.getMeasureValue()+"\","
        			+ "\"dateRegistered\": \""+currentDate+"\","
        			+ "\"measureType\" : {"
        				+ "\"measureType\" : \""+lf.getMeasureName()+"\"}}";
    	
    	res = service.request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(updateLifeStatus));
    	putResp = res.readEntity(String.class);
    	
    	if(res.getStatus() != 200 ){
    		return Response.status(400).build();
        }
		
		//GET RESULT COMPARISON
    	String ENDPOINT2 = "https://fast-tundra-9608.herokuapp.com/businesslogic/getResultComparison/"+idPerson+"/"+lf.getMeasureName();
    	DefaultHttpClient client1 = new DefaultHttpClient();
    	HttpGet request = new HttpGet(ENDPOINT2);
    	HttpResponse response = client1.execute(request);
    	
    	BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

    	StringBuffer result = new StringBuffer();
    	String line = "";
    	while ((line = rd.readLine()) != null) {
    	    result.append(line);
    	}

    	JSONObject o = new JSONObject(result.toString());
    	
    	if(response.getStatusLine().getStatusCode() != 200){
    		return Response.status(204).build();
    	}

    	//GET PICTURE
    	String ENDPOINT3 = "https://morning-lake-2272.herokuapp.com/storeservice/getPicture";
    	DefaultHttpClient client3 = new DefaultHttpClient();
    	HttpGet request3 = new HttpGet(ENDPOINT3);
    	HttpResponse response3 = client3.execute(request3);
    	
    	BufferedReader rd3 = new BufferedReader(new InputStreamReader(response3.getEntity().getContent()));
    	StringBuffer result3 = new StringBuffer();
    	String line3 = "";
    	
    	while ((line3 = rd3.readLine()) != null) {
    	    result3.append(line3);
    	}

    	JSONObject o3 = new JSONObject(result3.toString());
    	
    	if(response3.getStatusLine().getStatusCode() != 200){
    		return Response.status(204).build();
    	}
    	
    	//GET QUOTE
    	String ENDPOINT4 = "https://morning-lake-2272.herokuapp.com/storeservice/getQuote";
    	DefaultHttpClient client4 = new DefaultHttpClient();
    	HttpGet request4 = new HttpGet(ENDPOINT4);
    	HttpResponse response4 = client4.execute(request4);
    	
    	BufferedReader rd4 = new BufferedReader(new InputStreamReader(response4.getEntity().getContent()));
    	StringBuffer result4 = new StringBuffer();
    	String line4 = "";
    	
    	while ((line4 = rd4.readLine()) != null) {
    	    result4.append(line4);
    	}
    	
    	System.out.println(result4.toString());

    	JSONObject o4 = new JSONObject(result4.toString());
    	
    	if(response4.getStatusLine().getStatusCode() != 200){
    		System.out.println("IN");
    		return Response.status(204).build();
    	}
    	
		String buildXml = "";
    	buildXml = "<updatedLifeStatus>";
    	buildXml += "<measureValueUpdated>"+lf.getMeasureValue()+"</measureValueUpdated>";
    	buildXml += "<measureType>"+lf.getMeasureName()+"</measureType>";
    	buildXml += "</updatedLifeStatus>";
    	
    	buildXml += "<comparisonInformation>";
    	buildXml += "<result>"+o.getJSONObject("comparisonInfomation").getString("result")+"</result>";
    	buildXml += "<measure>"+o.getJSONObject("comparisonInfomation").getString("measure")+"</measure>";
    	buildXml += "<goalValue>"+o.getJSONObject("comparisonInfomation").getDouble("goalValue")+"</goalValue>";
    	buildXml += "<lifeStatusValue>"+o.getJSONObject("comparisonInfomation").getDouble("lifeStatusValue")+"</lifeStatusValue>";
    	buildXml += "</comparisonInformation>";
    	
    	buildXml += "<resultInformation>";
    	buildXml += "<picture_url>"+o3.getString("picture_url")+"</picture_url>";
    	buildXml += "<quote>"+o4.getString("quote")+"</quote>";
    	buildXml += "</resultInformation>";
    	
    	JSONObject xmlJSONObj = XML.toJSONObject(buildXml);
        String jsonPrettyPrintString = xmlJSONObj.toString(4);
        
        System.out.println(jsonPrettyPrintString);
        
        return Response.ok(jsonPrettyPrintString).build();	
    }
	
	
	@PUT
    @Path("/updatePersonGoal/{idPerson}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateGoal(@PathParam("idPerson") int idPerson, Goal goal) throws IOException {
		
		//PUT LIFESTATUS
    	String ENDPOINT = "https://morning-lake-2272.herokuapp.com/storeservice/updateGoal/"+idPerson;
    	ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		
		WebTarget service = client.target(ENDPOINT);

    	Response res = null;
		String putResp = null;
		
    	String updateGoal ="{"
        			+ "\"goalValue\": \""+goal.getGoalValue()+"\","
        			+ "\"measureType\" : {"
        				+ "\"measureType\" : \""+goal.getMeasureName()+"\"}}";
    	
    	res = service.request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(updateGoal));
    	putResp = res.readEntity(String.class);
    	
    	if(res.getStatus() != 200 ){
    		return Response.status(400).build();
    	}
    	
    	return Response.ok(goal).build();

    }
	
}