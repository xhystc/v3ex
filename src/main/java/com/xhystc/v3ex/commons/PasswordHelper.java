package com.xhystc.v3ex.commons;


import com.xhystc.v3ex.model.User;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.UUID;

public class PasswordHelper
{

	private String algorithmName = "MD5";
	private int hashIterations = 2;


	public PasswordHelper(String algorithmName,int hashIterations){
		this.algorithmName = algorithmName;
		this.hashIterations = hashIterations;
	}

	public void generatePassword(User user){
		String password = user.getPassword();

		String s = UUID.randomUUID().toString();
		user.setSalt(s);
		SimpleHash obj = new SimpleHash(algorithmName, password, user.getCredentialsSalt(), hashIterations);
		user.setPassword(obj.toHex());
	}

	public PasswordHelper(){}
	public String getAlgorithmName()
	{
		return algorithmName;
	}

	public void setAlgorithmName(String algorithmName)
	{
		this.algorithmName = algorithmName;
	}

	public int getHashIterations()
	{
		return hashIterations;
	}

	public void setHashIterations(int hashIterations)
	{
		this.hashIterations = hashIterations;
	}
}
