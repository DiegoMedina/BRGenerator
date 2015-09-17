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
	private List<TemplateObjectNode> nodes;
	
	public void setNodes(List<TemplateObjectNode> nodes) {
		this.nodes = nodes;
	}

	public enum Type {
	    PROPERTIES, PROPERTY, ATT, MODEL, TEXT
	}
	
	
	
	private String pPROPERTIES = "\\[properties.*?]";
	private Pattern PROPERTIES = Pattern.compile(pPROPERTIES);
	
	private String pATT = "\\[att.*?]";
	private Pattern ATT = Pattern.compile(pATT);
	
	private String pMODEL = "\\[model.*?]";
	private Pattern MODEL = Pattern.compile(pMODEL);
	
	private String pCUOTE = "\\[cuote.*?]";
	private Pattern CUOTE = Pattern.compile(pCUOTE);
	
	private String pPROPERTY = "\\[property.*?]";
	private Pattern PROPERTY = Pattern.compile(pPROPERTY);
	
	private String pELEMENT_PROPERTIES = "\\[properties.*?].+?\\[/properties]";
	private Pattern ELEMENT_PROPERTIES = Pattern.compile(pELEMENT_PROPERTIES,Pattern.DOTALL);
	
	//private String pELEMENT_PROPERTY = "\\[property.*?].+?\\[/property]";
	private String pELEMENT_PROPERTY = "\\[property.*?].+?\\[/property]|\\[property.*?/]";
	
	private Pattern ELEMENT_PROPERTY = Pattern.compile(pELEMENT_PROPERTY,Pattern.DOTALL);
	
	private String pELEMENT_ATT = "\\[att.*?].+?\\[/att]";
	private Pattern ELEMENT_ATT = Pattern.compile(pELEMENT_ATT,Pattern.DOTALL);
	
	private String pELEMENT_MODEL = "\\[model.*?].+?\\[/model]|\\[model.*?/]";
	private Pattern ELEMENT_MODEL = Pattern.compile(pELEMENT_MODEL,Pattern.DOTALL);
	
	//CONTENT
	//(?s)
	//private String pCONTENT_ELEMENT = "\\[.*?](.+?)\\[/.*?]|\\[(.*?)]";
	private String pCONTENT_ELEMENT = "\\[.+?](.*)\\[/.*?]|\\[(.*?)]";
	private Pattern CONTENT_ELEMENT = Pattern.compile(pCONTENT_ELEMENT,Pattern.DOTALL);
	
	
	public TemplateObject(TemplateObject to) 
	{
		this.setText(to.text);
		this.setFirstIndex(to.firstIndex);
		this.setLastIndex(to.lastIndex);
		this.setType(to.type);
		this.setContent(to.text);
		this.getNodes();
	}
	
	public TemplateObject(String text, int firstIndex, int lastIndex, Type type) 
	{
		this.setText(text);
		this.setFirstIndex(firstIndex);
		this.setLastIndex(lastIndex);
		this.setType(type);
		this.setContent(text);
		this.getNodes();
	}
	
	public TemplateObject(File origen) throws IOException
	{
		Path path = Paths.get(origen.getAbsolutePath());
		Charset charset = StandardCharsets.UTF_8;
		String texto = new String(Files.readAllBytes(path), charset);
		this.setType(Type.TEXT);
		this.setText(texto);
		this.setContent(texto);
		this.setFirstIndex(0);
		this.setLastIndex(texto.length()-1);
		
		this.getNodes();
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
	
	
	
	public List<TemplateObjectNode> getNodes()
	{
	    return this.nodes;
	}
	
	public TemplateObjectNode getNode()
	{
		Matcher matcher;
		TemplateObjectNode ton;
	    
		switch (this.type) 
		{
			case PROPERTIES:
				
				matcher = PROPERTIES.matcher(this.text);
				while (matcher.find()) 
			    {
			    	ton = new TemplateObjectNode(matcher.group(), TypeNode.PROPERTIES, matcher.start(0), matcher.end(0));
			    	return ton;
			    }
				
				break;
			case PROPERTY:
				
				matcher = PROPERTY.matcher(this.text);
				while (matcher.find()) 
			    {
			    	ton = new TemplateObjectNode(matcher.group(), TypeNode.PROPERTY, matcher.start(0), matcher.end(0));
			    	return ton;
			    }
				break;
			case ATT:
				
				matcher = ATT.matcher(this.text);
				while (matcher.find()) 
			    {
			    	ton = new TemplateObjectNode(matcher.group(), TypeNode.ATT, matcher.start(0), matcher.end(0));
			    	return ton;
			    }
			    
				break;   
			case MODEL:
				
				matcher = MODEL.matcher(this.text);
				while (matcher.find()) 
			    {
			    	ton = new TemplateObjectNode(matcher.group(), TypeNode.MODEL, matcher.start(0), matcher.end(0));
			    	return ton;
			    }
				break;
		default:
			break;
		}		
		
	    
	    return null;
		
	}
	
	public List<TemplateObjectNode> getNodesByType(TemplateObjectNode.TypeNode type)
	{
		Matcher matcher;
		List<TemplateObjectNode> tons = new ArrayList<TemplateObjectNode>();
	    
		switch (type) 
		{
			case PROPERTIES:
				
				matcher = PROPERTIES.matcher(this.content);
				while (matcher.find()) 
			    {
			    	TemplateObjectNode ton = new TemplateObjectNode(matcher.group(), type, matcher.start(0), matcher.end(0));
			    	tons.add(ton);
			    }
				
				break;
			case PROPERTY:
				
				matcher = PROPERTY.matcher(this.content);
				while (matcher.find()) 
			    {
			    	TemplateObjectNode ton = new TemplateObjectNode(matcher.group(), type, matcher.start(0), matcher.end(0));
			    	tons.add(ton);
			    }
				break;
			case ATT:
				
				matcher = ATT.matcher(this.content);
				while (matcher.find()) 
			    {
			    	TemplateObjectNode ton = new TemplateObjectNode(matcher.group(), type, matcher.start(0), matcher.end(0));
			    	tons.add(ton);
			    }
			    
				break;   
			case MODEL:
				
				matcher = MODEL.matcher(this.content);
				while (matcher.find()) 
			    {
			    	TemplateObjectNode ton = new TemplateObjectNode(matcher.group(), type, matcher.start(0), matcher.end(0));
			    	tons.add(ton);
			    }
				break;
			case CUOTE:
				
				matcher = CUOTE.matcher(this.content);
				while (matcher.find()) 
			    {
			    	TemplateObjectNode ton = new TemplateObjectNode(matcher.group(), type, matcher.start(0), matcher.end(0));
			    	tons.add(ton);
			    }
				break;
		}		
		
	    
	    return tons;
		
	}
	
	public void setNodes()
	{
		Matcher matcher;
		
		matcher = PROPERTIES.matcher(this.text);

		while (matcher.find()) 
	    {
	    	TemplateObjectNode ton = new TemplateObjectNode(matcher.group(), TypeNode.PROPERTIES);
	    	this.nodes.add(ton);	    	
	    }
	    
	    matcher = PROPERTY.matcher(this.text);
		
	    while (matcher.find()) 
	    {
	    	TemplateObjectNode ton = new TemplateObjectNode(matcher.group(), TypeNode.PROPERTY);
	    	this.nodes.add(ton);	    	
	    }
	    
	    matcher = ATT.matcher(this.text);
		
	    while (matcher.find()) 
	    {
	    	TemplateObjectNode ton = new TemplateObjectNode(matcher.group(), TypeNode.ATT);
	    	this.nodes.add(ton);	    	
	    }
		
	    matcher = MODEL.matcher(this.text);
		
	    while (matcher.find()) 
	    {
	    	TemplateObjectNode ton = new TemplateObjectNode(matcher.group(), TypeNode.MODEL);
	    	this.nodes.add(ton);	    	
	    }
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
	
	public TemplateObject replaceContentBy(String value)
	{
		this.content = value;
		return this;
	}
	
	public TemplateObject replace(TemplateObject element)
	{
		String resultado = this.content.substring(0, element.getFirstIndex()) + element.getContent() + this.content.substring(element.getLastIndex(), this.content.length()); 
		this.content = resultado;
		return this;
	}
	
	public TemplateObject append(TemplateObject element)
	{
		String resultado = this.content + element.content; 
		this.content = resultado;
		return this;
	}
	
	public TemplateObject replace(TemplateObjectNode element)
	{
		String resultado = this.content.substring(0, element.getFirstIndex()) + element.getContent() + this.content.substring(element.getLastIndex(), this.content.length()); 
		this.content = resultado;
		return this;
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
				
			case ATT:
				
				matcher = ELEMENT_ATT.matcher(this.content);
			    
			    while (matcher.find()) 
			    {
			        TemplateObject to = new TemplateObject(matcher.group(),matcher.start(0),matcher.end(0), Type.ATT);
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
	
}
