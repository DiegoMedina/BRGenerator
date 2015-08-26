package com.brgenerator.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateObject 
{
	private String content = "";
	private int firstIndex;
	private int lastIndex;
	
	private String pATTRIBUTES = "(\\S+)=\"(.*?)\"";
	private Pattern ATTRIBUTES = Pattern.compile(pATTRIBUTES);
	
	private String pPROPERTIES = "\\[properties.*?]";
	private Pattern PROPERTIES = Pattern.compile(pPROPERTIES);
	
	public TemplateObject(String content, int firstIndex, int lastIndex) 
	{
		this.setContent(content);
		this.setFirstIndex(firstIndex);
		this.setLastIndex(lastIndex);
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public void appendContent(String content) {
		this.content = (this.content.concat(content));
	}
	
	public int getFirstIndex() {
		return firstIndex;
	}
	
	public void setFirstIndex(int firstIndex) {
		this.firstIndex = firstIndex;
	}
	
	public int getLastIndex() {
		return lastIndex;
	}
	
	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}
	
	public String getType()
	{
		String tagContent = this.content.substring(1, this.content.length()-1);
		String[] values = tagContent.split(":");
		if(values.length >= 0)
		{
			return values[0];
		}
		
		return "";
	}
	
	public Boolean isElement()
	{
		String tagContent = this.content.substring(1, this.content.length()-1);
		String[] values = tagContent.split(":");
		if(values.length == 2)
		{
			return true;
		}
		
		return false;
	}
	
	public List<TemplateObjectAtt> getAttributes(String nodo)
	{
		final Matcher matcher = ATTRIBUTES.matcher(nodo);
	    List<TemplateObjectAtt> toa = new ArrayList<TemplateObjectAtt>();
	    
	    while (matcher.find()) 
	    {
	    	TemplateObjectAtt to = new TemplateObjectAtt(matcher.group(0), matcher.group(1));
	    	toa.add(to);
	    }
	    
	    return toa;
	}
	
	public TemplateObjectNode getNode(TemplateObjectNode.TypeNode tipo)
	{
		
		switch (tipo) 
		{
			case PROPERTIES:
				
				final Matcher matcher = PROPERTIES.matcher(this.content);
			    TemplateObjectNode ton = new TemplateObjectNode();
				
			    while (matcher.find()) 
			    {
			    	ton.setName("Properties");
			    	ton.setAttrs(this.getAttributes(matcher.group()));
			    	return ton;
			    }
			    
			break;
			case PROPERTY:
				
			break;
			case ATTR:
				
			break;
			case MODEL:
			break;
			default:
			break;
		}
		
	    return null;
	}
	
	public String getAttr()
	{
		String pTYPE = "\\[\\S[a-z]*?:(\\S[a-z]*?)\\]|\\[\\S[a-z]*?:(\\S[a-z]*?)\\:\\S[a-z]*?\\]";
		Pattern TYPE = Pattern.compile(pTYPE);		
		Matcher matcher = TYPE.matcher(this.content);

	    while (matcher.find()) 
	    {
	    	if(matcher.group(1) != null && matcher.group(1)!= "")
	    	{
	    		return matcher.group(1);
	    	}
	    	else if(matcher.group(2) != null && matcher.group(2)!= "")
	    	{
	    		return matcher.group(2);
	    	}
	    	else
	    	{
	    		return "";
	    	}
	    }
	    
	    return "";
		
	}
	
}
