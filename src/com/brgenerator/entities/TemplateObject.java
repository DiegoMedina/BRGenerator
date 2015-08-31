package com.brgenerator.entities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.brgenerator.entities.TemplateObjectNode.TypeNode;

public class TemplateObject 
{
	private String content = "";
	private int firstIndex;
	private int lastIndex;
	private Type type;
	private String text = "";
	
	public enum Type {
	    PROPERTIES, PROPERTY, ATTR, MODEL, TEXT
	}
	
	private String pATTRIBUTES = "(\\S+)=\"(.*?)\"";
	private Pattern ATTRIBUTES = Pattern.compile(pATTRIBUTES);
	
	private String pPROPERTIES = "\\[properties.*?]";
	private Pattern PROPERTIES = Pattern.compile(pPROPERTIES);
	
	private String pPROPERTY = "\\[property.*?]";
	private Pattern PROPERTY = Pattern.compile(pPROPERTY);
	
	private String pELEMENT_PROPERTIES = "\\[properties.*?].+?\\[/properties]";
	private Pattern ELEMENT_PROPERTIES = Pattern.compile(pELEMENT_PROPERTIES,Pattern.DOTALL);
	
	private String pELEMENT_PROPERTY = "\\[property.*?].+?\\[/property]";
	private Pattern ELEMENT_PROPERTY = Pattern.compile(pELEMENT_PROPERTY,Pattern.DOTALL);
	
	private String pELEMENT_ATT = "\\[att.*?].+?\\[/att]";
	private Pattern ELEMENT_ATT = Pattern.compile(pELEMENT_ATT,Pattern.DOTALL);
	
	private String pELEMENT_MODEL = "\\[model.*?].+?\\[/model]";
	private Pattern ELEMENT_MODEL = Pattern.compile(pELEMENT_MODEL,Pattern.DOTALL);
	
	//CONTENT
	
	private String pCONTENT_ELEMENT = "\\[.*?](.+?)\\[/.*?]";
	private Pattern CONTENT_ELEMENT = Pattern.compile(pCONTENT_ELEMENT,Pattern.DOTALL);
	
	
	public TemplateObject(String text, int firstIndex, int lastIndex, Type type) 
	{
		this.setText(text);
		this.setFirstIndex(firstIndex);
		this.setLastIndex(lastIndex);
		this.setType(type);
		this.setContent(text);
	}
	
	public TemplateObject(File origen) throws IOException
	{
		Path path = Paths.get(origen.getAbsolutePath());
		Charset charset = StandardCharsets.UTF_8;
		String texto = new String(Files.readAllBytes(path), charset);
		this.setText(texto);
		this.setContent(texto);
		this.setFirstIndex(0);
		this.setLastIndex(texto.length()-1);
		this.setType(Type.TEXT);
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Type getType() {
		return this.type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String text) 
	{
		if(this.type == Type.TEXT)
		{
			this.content = text;
		}
		else
		{
			final Matcher matcher;
				
			matcher = CONTENT_ELEMENT.matcher(text);
	    
		    while (matcher.find()) 
		    {
		        this.content = matcher.group(1);
		    }

		}
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
	
	public List<TemplateObject> getChildElements(TemplateObjectNode.TypeNode tipo)
	{
		final Matcher matcher;
		List<TemplateObject> tos = new ArrayList<TemplateObject>();
	    
		switch (tipo) 
		{
			case PROPERTIES:
				
				matcher = ELEMENT_PROPERTIES.matcher(this.content);
			    
			    while (matcher.find()) 
			    {
			        TemplateObject to = new TemplateObject(matcher.group(),matcher.start(0),matcher.end(0), Type.PROPERTIES);
			        tos.add(to);
			    }
			    
			    return tos;

			case PROPERTY:
				
				matcher = ELEMENT_PROPERTY.matcher(this.content);
			    
			    while (matcher.find()) 
			    {
			        TemplateObject to = new TemplateObject(matcher.group(),matcher.start(0),matcher.end(0), Type.PROPERTY);
			        tos.add(to);
			    }
			    
			    return tos;
				
			case ATTR:
				
				matcher = ELEMENT_ATT.matcher(this.content);
			    
			    while (matcher.find()) 
			    {
			        TemplateObject to = new TemplateObject(matcher.group(),matcher.start(0),matcher.end(0), Type.ATTR);
			        tos.add(to);
			    }
			    
			    return tos;
			    
			case MODEL:
				
				matcher = ELEMENT_MODEL.matcher(this.content);
			    
			    while (matcher.find()) 
			    {
			        TemplateObject to = new TemplateObject(matcher.group(),matcher.start(0),matcher.end(0), Type.MODEL);
			        tos.add(to);
			    }
			    
			    return tos;
		}
		
	    return null;
	}
	
	public TemplateObjectNode getNode(TemplateObjectNode.TypeNode tipo)
	{
		
		final Matcher matcher;
		TemplateObjectNode ton = new TemplateObjectNode();
		
		switch (tipo) 
		{
			case PROPERTIES:
				
				matcher = PROPERTIES.matcher(this.content);

			    while (matcher.find()) 
			    {
			    	ton.setName("Properties");
			    	ton.setAttrs(this.getAttributes(matcher.group()));
			    	return ton;
			    }
			    
			break;
			case PROPERTY:
				
				matcher = PROPERTY.matcher(this.content);
				
			    while (matcher.find()) 
			    {
			    	ton.setName("Property");
			    	ton.setAttrs(this.getAttributes(matcher.group()));
			    	return ton;
			    }
				
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
