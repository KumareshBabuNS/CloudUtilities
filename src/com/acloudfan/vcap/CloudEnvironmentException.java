package com.acloudfan.vcap;

public class CloudEnvironmentException 
extends Exception {

	public CloudEnvironmentException() {
		super();
	}

	public CloudEnvironmentException(String message, Throwable cause) {
		super(message, cause);
	}

	public CloudEnvironmentException(String message) {
		super(message);
	}

	public CloudEnvironmentException(Throwable cause) {
		super(cause);
	}

}
