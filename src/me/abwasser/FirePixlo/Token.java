package me.abwasser.FirePixlo;

import java.util.ArrayList;
import java.util.Random;


public class Token {

	public static String alphabeth = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
	public static final int TotalLength = 6;
	public static ArrayList<String> tokens = new ArrayList<>();	

	public static String generateToken() {
		char[] alpha = alphabeth.toCharArray();
		String token = "";
		for (int i = 0; i != TotalLength; i++) {
			char c = alpha[new Random().nextInt(alpha.length - 1)];
			token += c;
		}
		return token;
	}
	public static String generateToken(int length) {
		char[] alpha = alphabeth.toCharArray();
		String token = "";
		for (int i = 0; i != length; i++) {
			char c = alpha[new Random().nextInt(alpha.length - 1)];
			token += c;
		}
		return token;
	}
	
}
