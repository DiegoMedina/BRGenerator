package com.brgenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.brgenerator.TemplateManager;
import com.brgenerator.entities.Model;

import org.atteo.*;;

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
	
	public static void build(Model modelo, File fuente, File destino) throws IOException, FileNotFoundException
	{
		System.out.println("Se encontro template " + fuente.getName() + " para modelo " + modelo.getName());
		Document doc = Jsoup.parse(fuente, "UTF-8");
		doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
		Elements links = doc.getAllElements();
		
		
		
		for (Element link : links) 
		{
			  TextNode text;
			  Inflector inf = new Inflector();
			  
			  System.out.println("link.nodeName(): " + link.nodeName());
			  switch (link.nodeName()) 
			  {
				  case "objectlowername":
					  text = new TextNode(inf.singularize(modelo.getName()).toLowerCase(), "");
					  link.replaceWith(text);
				  break;
				  case "objectname":
					  text = new TextNode(modelo.getName(), "");
					  link.replaceWith(text);
				  break;
				  case "objectpluralname":
					  text = new TextNode(inf.pluralize(modelo.getName()), "");
					  link.replaceWith(text);
				  break;
				  default:
				  break;
			  }
			  
			  
		}

		
		System.out.println("Data nodo link.text(): " + doc.getElementsByTag("body").text() + "\n" +
		// "link.className(): " + links.className() + "\n" +
		// "link.cssSelector(): " + links.cssSelector() + "\n" +
		// "link.data(): " + links.data() + "\n" +
		  "link.html(): " + doc.getElementsByTag("body").html() + "\n" +
		// "link.id(): " + links.id() + "\n" +
		//"link.nodeName(): " + links.nodeName() + "\n" +
		  "link.outerHtml(): " + doc.getElementsByTag("body").outerHtml() + "\n" +
		//"link.ownText(): " + links.ownText() + "\n" +
		//"link.tagName(): " + links.tagName() + "\n" +
		  "link.toString(): " + doc.getElementsByTag("body").toString() + "\n" +
		  "link.val(): " + doc.getElementsByTag("body").val() + "\n\n" );
	
		String texto = doc.getElementsByTag("body").html();
		BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destino), "UTF-8"));
		fileWriter.write(texto);
		fileWriter.flush();
		fileWriter.close();
		System.out.println("Archivo creado de " + fuente + " a " + destino);
	}
	
	

}