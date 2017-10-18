package com.emjaay.mdb.communication.result;

import android.os.Bundle;

public class ApiResult {

	public static final int ERROR_SERVER				= -1;
	public static final int ERROR_MISSING_PARAMETER		= 1;
	public static final int ERROR_EMAIL_DONT_EXISTS		= 2;
	public static final int ERROR_WRONG_PASSWORD		= 3;
	public static final int ERROR_EMAIL_ALREADY_EXISTS	= 5;
	
	private boolean success;
	private String response;
	private Bundle bundle;
	private int errorCode;
	
	public ApiResult() {
	}
	
	public ApiResult(boolean success, String response) {
		this.success = success;
		this.response = response;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}
	
	public Bundle getBundle() {
		return bundle;
	}
	
	public void setResponse(String response) {
		this.response = response;
	}
	
	public String getResponse() {
		return response;
	}
	
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
}
