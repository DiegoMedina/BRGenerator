package com.brgenerator.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.brgenerator.entities.TemplateObject.Type;
import com.brgenerator.entities.TemplateObjectAtt.TypeAtt;


public class TemplateObjectNode
{
	private TypeNode type;
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
	
	private String content;
	private List<TemplateObjectAtt> attrs;
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	private int firstIndex;
	private int lastIndex;
	
	private String pATTRIBUTES = "(\\S+)=\"(.*?)\"";
	private Pattern ATTRIBUTES = Pattern.compile(pATTRIBUTES);
	
	public enum TypeNode {
	    PROPERTIES, PROPERTY, ATT, MODEL
	}
	
	public TemplateObjectNode() {
		
	}
	
	public TemplateObjectNode(String node, TypeNode type) 
	{
		super();
		this.type = type;
		this.attrs = this.getAttributes(node);
		this.content = node;
	}
	
	public TemplateObjectNode(String node, TypeNode type, int firstIndex, int lastIndex) 
	{
		super();
		this.type = type;
		this.attrs = this.getAttributes(node);
		this.content = node;
	}

	public TypeNode getType() {
		return this.type;
	}

	public void setName(TypeNode type) {
		this.type = type;
	}

	public List<TemplateObjectAtt> getAttrs() {
		return attrs;
	}
	
	public TemplateObjectAtt getAttrByType(TypeAtt tipo) 
	{
		for (int i = 0; i < this.getAttrs().size(); i++) 
		{
			if(this.getAttrs().get(i).getKey().toLowerCase() == tipo.toString().toLowerCase())
			{
				return this.getAttrs().get(i);
			}
		}
		
		return null;
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
	
	public void setAttrs(List<TemplateObjectAtt> attrs) {
		this.attrs = attrs;
	}
	
}
