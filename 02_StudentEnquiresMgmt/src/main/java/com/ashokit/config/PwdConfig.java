package com.ashokit.config;

import org.jasypt.util.text.AES256TextEncryptor;

public class PwdConfig {
	
	
	public String pwdEncryptor(String pwd) {
		AES256TextEncryptor encryptor=new AES256TextEncryptor();
		String encrypt = encryptor.encrypt(pwd);
		return encrypt;
	}
}
