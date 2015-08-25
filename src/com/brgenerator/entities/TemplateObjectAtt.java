package com.brgenerator.entities;


public class TemplateObjectAtt
{
	private String key = "";
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private String value = "";
	
	public TemplateObjectAtt(String key, String value) 
	{
		this.setKey(key);
		this.setValue(value);
	}
	
	
	
}
