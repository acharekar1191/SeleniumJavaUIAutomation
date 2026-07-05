package com.qa.opencart.utils;

import java.util.Arrays;
import java.util.List;

public class Constants {
	
	public static final String LOGIN_PAGE_TITLE = "QKart";
	public static final String REGISTER_PAGE_HEADER = "Register";
	public static final String LOGIN_PAGE_URL_FRACTION = "/login";
	public static final String LOGIN_PAGE_HEADER = "Login";
	public static final String HOME_PAGE_URL= "https://crio-qkart-frontend-qa.vercel.app/";
	public static final String REGISTER_PAGE_URL_FRACTION="/register";
	public static final List<String> FOOTER_LISTS = Arrays.asList("Privacy policy","About us","Terms of Service");
	public static final List<String> PRODUCT_LISTS_SHOES = Arrays.asList("Roadster Mens Running Shoes","Nike Mens Running Shoes");
	public static final String CHECKOUT_PAGE_URL_FRACTION="/checkout";
	public static final String THANKS_PAGE_URL_FRACTION="/thanks";
	
	public static final String REGISTER_SUCCESS_MSG = "Registered Successfully";
	public static final String LOGIN_SUCCESS_MSG = "Logged in successfully";	
	public static final List<String> expectedSizes = Arrays.asList("6","7","8","9","10");
	public static final String Roadster_price = "$30";
	
	public static final String ADDRESS1 = "GK Society A wing 1101 Akola Maharashtra";
	public static final String ADDRESS2 = "SKE Skyes , Road 411899 MP";
	
	
	/**
	 * INVALID_LOGIN - ERROR MESSAGES
	 */
	public static final String INCORRECT_PASSWORD_MSG = "Password is incorrect";
	public static final String EMPTY_USERNAME_MSG = "Username is a required field";
	public static final String EMPTY_PASSWORD_MSG = "Password is a required field";
	public static final String USERNAME_NOT_EXISTS_MSG = "Username does not exist";

}
