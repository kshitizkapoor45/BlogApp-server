package com.blog.exceptions;

public class ResourceNotFound extends RuntimeException {
	
	String resourceName;
	String feildName;
	long feildValue;
	
	public ResourceNotFound(String resourceName, String feildName, long feildValue) {
		super(String.format("%s not found with %s : '%s'", resourceName, feildName, feildValue));
		this.resourceName = resourceName;
		this.feildName = feildName;
		this.feildValue = feildValue;
	}
	
   
 
	

}
