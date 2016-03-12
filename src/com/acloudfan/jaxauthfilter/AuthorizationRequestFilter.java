package com.acloudfan.jaxauthfilter;

import java.awt.image.RasterFormatException;
import java.io.IOException;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.acloudfan.vcap.CloudEnvironmentException;
import com.acloudfan.vcap.CloudEnvironmentFactory;


/**
 * This talks about how to write filters and interceptors
 * https://jersey.java.net/documentation/latest/filters-and-interceptors.html
 * 
 * This is the library for Bluemix Spring connectors
 * https://github.com/IBM-Bluemix/bluemix-cloud-connectors
 * 
 * The environment variable in which the name of the UPS will be set
 * 		X_AUTHORIZATION_UPS_NAME
 * 
 * The caller will need to set the following headers
 * 		X_MS_CLIENT_KEY      Credentials Json field >>>  clientkey
 *      X_MS_CLIENT_SECRET   Credentials Json field >>>  clientsecret
 *  
 * Create the UPS
 * cf cups MSRestAuthorization -p '{"clientkey":"clientkey", "clientsecret":"clientsecret"}'
 */
@AuthorizeCall
@Provider
public class AuthorizationRequestFilter implements ContainerRequestFilter {

	private	static	Map<String, Object> authorizationCredentials = null;
	
	static{
		String authUPSName = System.getenv("X_AUTHORIZATION_UPS_NAME");
		try{
			if(authUPSName != null){
				authorizationCredentials = CloudEnvironmentFactory.getUPSCredentialsByName(authUPSName);
			}
		}catch(CloudEnvironmentException cee){
			throw new RuntimeException("Error initializing Authorization Filter",cee);
		}
		if(authorizationCredentials == null) throw new RuntimeException("Null Credentials found for X_AUTHORIZATION_UPS_NAME="+authUPSName);
	}
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String key = requestContext.getHeaderString("X_MS_CLIENT_KEY");
		String secret = requestContext.getHeaderString("X_MS_CLIENT_SECRET");
		
		/** If there is a mismatch of either key or secret send back an unauth message **/
		if(!(authorizationCredentials.get("clientkey").toString().equals(key) ||
				authorizationCredentials.get("clientsecret").toString().equals(secret))){
			
			requestContext.abortWith(Response
	                .status(Response.Status.UNAUTHORIZED)
	                .entity("Cannot access the resource.")
	                .build());
		}
		
	}

}
