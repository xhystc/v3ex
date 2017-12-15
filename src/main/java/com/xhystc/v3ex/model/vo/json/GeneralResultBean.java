package com.xhystc.v3ex.model.vo.json;

import java.io.Serializable;
import java.util.List;

public class GeneralResultBean implements Serializable
{
	private int code;
	private String hint;

	public GeneralResultBean(int code, String hint)
	{
		this.code = code;
		this.hint = hint;
	}
	public GeneralResultBean(){}

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public String getHint()
	{
		return hint;
	}

	public void setHint(String hint)
	{
		this.hint = hint;
	}


}
