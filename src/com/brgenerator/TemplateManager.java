package com.brgenerator;

import java.io.File;

import com.brgenerator.TemplateManager;

public class TemplateManager {
	
	static File[] files;
	static String pathRoot = "";
	static String[] pathTemplates;
	public static final String DOT_SEPARATOR = ".";
	public static final String TEMPLATE_EXTENSION = ".brt";
	public static final String XML_EXTENSION= ".xml";
	
	public static void main(String path)
	{
		pathRoot = path;
		files = new File(path).listFiles();
	}
	
	public void generateCode(File src)
	{
		
	}
	
	public static String getFileExtension(File file) 
	{ 
		if (file == null) 
		{ 
			return null; 
		} 
		String name = file.getName();
		
		int extIndex = name.lastIndexOf(TemplateManager.DOT_SEPARATOR); 
		
		if (extIndex == -1) 
		{ 
			return ""; 
		} 
		else 
		{ 
			return name.substring(extIndex + 1); 
		}
	}
	
	public static Boolean isTemplate(File archivo)
	{
		return getFileExtension(archivo) == TEMPLATE_EXTENSION;
	}
	
	

}