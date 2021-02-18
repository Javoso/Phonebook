package com.phonebook.util;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Criptografia implements Serializable {
	
	private static final long serialVersionUID = -2446728688093930095L;
	
	private transient MessageDigest md;
	
	public Criptografia() {
		try {
			this.md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String criptografar(String senha) {
		if(senha == null || senha.isEmpty())
			throw new IllegalArgumentException("A senha é obrigatória");
		
		return new String(hexCodes(md.digest(senha.getBytes())));
	}

	private char[] hexCodes(byte[] text) {
		char[] hexOutput = new char[text.length * 2];
		
		String hexString;
		
		for (int i = 0; i < text.length; i++) {
			hexString = "00" + Integer.toHexString(text[i]);
			hexString.getChars(hexString.length() - 2,
					hexString.length(), hexOutput, i * 2);
		}
		
		return hexOutput;
	}
	
}