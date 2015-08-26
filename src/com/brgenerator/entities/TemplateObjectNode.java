package com.brgenerator.entities;

import java.util.List;

import com.brgenerator.entities.TemplateObjectAtt.TypeAtt;


public class TemplateObjectNode
{
	private String name = "";
	private List<TemplateObjectAtt> attrs;
	
	public enum TypeNode {
	    PROPERTIES, PROPERTY, ATTR, MODEL
	}
	
	public TemplateObjectNode() {
		
	}
	
	public TemplateObjectNode(String name, List<TemplateObjectAtt> attrs) {
		super();
		this.name = name;
		this.attrs = attrs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public void setAttrs(List<TemplateObjectAtt> attrs) {
		this.attrs = attrs;
	}
	
}
