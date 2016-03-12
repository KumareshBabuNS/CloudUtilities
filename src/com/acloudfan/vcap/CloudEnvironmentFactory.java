package com.acloudfan.vcap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Reads the VCAP_SERVICES environment property
 * Reads the Environment property
 * 
 * THIS IS A WORK IN PROGRESS
 * 
 * Good Jackson Tutorial
 * http://www.journaldev.com/2324/jackson-json-processing-api-in-java-example-tutorial
 */
public class CloudEnvironmentFactory {
	
	public static String	VCAP_SERVICES_JSON;
	
	static{
		if(System.getenv("VCAP_SERVICES_DEVELOPMENT") != null){
			VCAP_SERVICES_JSON = System.getenv("VCAP_SERVICES_DEVELOPMENT");
		} else {
			VCAP_SERVICES_JSON = System.getenv("VCAP_SERVICES");
		}
	}

	/**
	 * Returns the user provided service by ID as a Map or returns a null
	 */
	public static Map<String, Object> getUPSByName(String name)
	throws CloudEnvironmentException{
		Map<String,Object> myMap = new HashMap<String, Object>();
		ObjectMapper om = new ObjectMapper();
		
		try{
			myMap = om.readValue(VCAP_SERVICES_JSON, HashMap.class);
			Map map = null;
			
			ArrayList al = (ArrayList) myMap.get("user-provided");
			System.out.println(myMap);
			for(Object service : al){
				map = (Map) service;
				if(map.get("name").equals(name)){
					return map;
				}
			}
		}catch(IOException io){
			throw new CloudEnvironmentException(io);
		}
		return null;
	}
	
	/**
	 * Returns the user provided service CREDENTIALS by Name as a Map or returns a null
	 */
	public static Map<String, Object> getUPSCredentialsByName(String name)
	throws CloudEnvironmentException{
		Map map = getUPSByName(name);
		if(map == null) return null;
		return (Map) map.get("credentials");
	}
	
	/**
	 * Testing 
	 */
	public static void main(String[] args)
	throws Exception{
		String json = "{\"user-provided\":[{\"name\":\"YuckYuck\",\"label\":\"user-provided\",\"tags\":[],\"credentials\":{\"password\":\"secret\",\"username\":\"client\"},\"syslog_drain_url\":\"\"},{\"name\":\"CuckCuck\",\"label\":\"user-provided\",\"tags\":[],\"credentials\":{\"password\":\"secret\",\"username\":\"client\"},\"syslog_drain_url\":\"\"}],\"cleardb\":[{\"name\":\"ClearDB MySQL Database-1r\",\"label\":\"cleardb\",\"tags\":[\"Data Stores\",\"DBA\",\"Bluemix\",\"Platform\",\"ibm_dedicated_public\",\"Infrastructure\",\"Service\",\"data_management\",\"IT Operations\",\"ibm_third_party\"],\"plan\":\"spark\",\"credentials\":{\"jdbcUrl\":\"jdbc:mysql://us-cdbr-iron-east-03.cleardb.net/ad_98d5d4afcc97d71?user=b9810b6ac48d12&password=99a88585\",\"uri\":\"mysql://b9810b6ac48d12:99a88585@us-cdbr-iron-east-03.cleardb.net:3306/ad_98d5d4afcc97d71?reconnect=true\",\"name\":\"ad_98d5d4afcc97d71\",\"hostname\":\"us-cdbr-iron-east-03.cleardb.net\",\"port\":\"3306\",\"username\":\"b9810b6ac48d12\",\"password\":\"99a88585\"}}]}";
		VCAP_SERVICES_JSON = json;
		
		System.out.println(getUPSByName("YuckYuck"));
		System.out.println(getUPSCredentialsByName("YuckYuck"));
	}
}
