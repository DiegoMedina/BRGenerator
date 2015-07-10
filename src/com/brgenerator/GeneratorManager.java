package com.brgenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.brgenerator.entities.Model;

import javax.servlet.ServletContext;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;


public class GeneratorManager
{
	
	static String pathRoot = "";
	static String pathDest = "/Base\\Generated\\";
	static List<Model> modelos = new ArrayList<Model>();
	private ServletContext servletContext;
	
	public GeneratorManager(ServletContext sc)
	{
		servletContext = sc;
	}
	
	public void loadModels()
	{
		modelos.clear();
		System.out.println("Carpeta con recursos: " + servletContext.getRealPath("/WEB-INF/resources/Model/"));
		File srcFolder = new File( servletContext.getRealPath("/WEB-INF/resources/Model/"));
		
		//make sure source exists
    	if(!srcFolder.exists())
    	{
    		System.out.println("Directory Model does not exist.");
           //just exit
           System.exit(0);
        }
    	else
    	{
    	    String files[] = srcFolder.list();
    	    
       		for (String file : files) 
       		{
       			File srcFile = new File(srcFolder, file);
       			if(srcFile.getName().endsWith(TemplateManager.XML_EXTENSION))
        		{
       				try
       				{
	       				JAXBContext jaxbContext = JAXBContext.newInstance(Model.class);
	       				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	       				Model modelo = (Model) jaxbUnmarshaller.unmarshal(srcFile);
	       				System.out.println(modelo);
	       				modelos.add(modelo);
	        		} 
       				catch (JAXBException e) 
       				{
	        			e.printStackTrace();
	        		}
        		}
       		}
        }
    	
    	for (Model model : modelos) 
    	{
    		System.out.println("Se cargo modelo: " + model.getName());
		}
    	
    }
	
	public void run()
	{
		System.out.println("Carpeta con recursos: " + servletContext.getRealPath("/WEB-INF/resources/Model/"));
		File srcFolder = new File(servletContext.getRealPath("/WEB-INF/resources/Base/"));
//		File srcFolder = new File(pathRoot);
    	File destFolder = new File(servletContext.getRealPath("/WEB-INF/resources/Destino/"));

    	//make sure source exists
    	if(!srcFolder.exists())
    	{
           System.out.println("Directory does not exist.");
           //just exit
           System.exit(0);
 
        }
    	else
    	{
           try
           {
        	   loadModels();
        	   generate(srcFolder,destFolder);
           }
           catch(IOException e)
           {
        	e.printStackTrace();
        	//error, just exit
                System.exit(0);
           }
        }
    	
    	System.out.println("Done");
	}
	
	public void generate(File src, File dest) throws IOException
	{
    	if(src.isDirectory())
    	{
    		//if directory not exists, create it
    		if(!dest.exists())
    		{
    		   dest.mkdir();
    		   System.out.println("Directory copied from "
                              + src + "  to " + dest);
    		}
 
    		//list all the directory contents
    		String files[] = src.list();
 
    		for (String file : files)
    		{
    		   //construct the src and dest file structure
    		   File srcFile = new File(src, file);
    		   File destFile = new File(dest, file);
    		   //recursive copy
    		   generate(srcFile,destFile);
    		}
 
    	}
    	else
    	{
    		if(src.getName().endsWith(TemplateManager.TEMPLATE_EXTENSION))
    		{
    			for (Model model : modelos) 
    			{
    				System.out.println("Se encontro template " + src.getName() + " para modelo " + model.getName());
//    				Document doc = Jsoup.parse(src);
//    				Elements links = doc.getAllElements();
    				
    				// Parse str into a Document
    				Document doc = Jsoup.parse(src, "UTF-8");
    				doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
    				// Get back the string of the body.
    				Elements links = doc.getAllElements();
    				
    				
    				for (Element link : links) 
    				{
    			
						  TextNode text;
						  
						  System.out.println("link.nodeName(): " + link.nodeName());
						  switch (link.nodeName()) 
						  {
							  case "objectlowername":
								  text = new TextNode("objeto", "");
								  link.replaceWith(text);
							  break;
							  case "objectname":
								  text = new TextNode("Objeto", "");
								  link.replaceWith(text);
							  break;
							  case "objectpluralname":
								  text = new TextNode("Objetos", "");
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
    				
//    				BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(doc.getElementsByTag("body").text()), "UTF-8"));
//    				fileWriter.write(dest.toString());
//    				fileWriter.close();
    			
    				String texto = doc.getElementsByTag("body").html();
    				BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest), "UTF-8"));
    				fileWriter.write(texto);
    				fileWriter.flush();
    				fileWriter.close();
    				System.out.println("Archivo creado de " + src + " a " + dest);
    				
    				
    				//if file, then copy it
    	    		//Use bytes stream to support all file types
//    				String texto = doc.getElementsByTag("body").html();
//        			InputStream in = new FileInputStream(src);
//    	    		OutputStream out = new FileOutputStream(dest); 
//        	        
//    	    		 byte[] buffer = doc.getElementsByTag("body").text().getBytes();
//     
//    	    		 int length;
//        	        //copy the file content in bytes 
//        	        while ((length = in.read(buffer)) > 0){
//         	    	   out.write(buffer, 0, length);
//         	        }
//      
//         	        in.close();
//         	        out.close();
//         	       System.out.println("Archivo creado de " + src + " a " + dest);
    				
//    				Writer writer = new PrintWriter(dest, texto);
//    				writer.write(doc.getElementsByTag("body").text());
//    				writer.close();
//    				System.out.println("Archivo creado de " + src + " a " + dest);
    				
//    				OutputStream out = new FileOutputStream(dest);
//    				 byte[] buffer = new byte[1024];
//    				 int length;
//    				 while ((length = in.read(buffer)) > 0){
//    	    	    	   out.write(buffer, 0, length);
//    	    	        }
//    	 
//    	    	        in.close();
//    	    	        out.close();
//    	    	        System.out.println("File copied from " + src + " to " + dest);
				}
    		}
    		else
    		{
	    		//if file, then copy it
	    		//Use bytes stream to support all file types
    			InputStream in = new FileInputStream(src);
	    		OutputStream out = new FileOutputStream(dest); 
    	        
	    		 byte[] buffer = new byte[1024];
 
	    		 int length;
    	        //copy the file content in bytes 
    	        while ((length = in.read(buffer)) > 0){
     	    	   out.write(buffer, 0, length);
     	        }
  
     	        in.close();
     	        out.close();
     	       System.out.println("File copied from " + src + " to " + dest);
    		}
    	}
    }
	
	
	public static void showFiles(File[] files) {
		for (File file : files) {
	        if (file.isDirectory()) {
	            System.out.println("Directory: " + file.getName());
	            showFiles(file.listFiles()); // Calls same method again.
	        } else {
	            System.out.println("File: " + file.getName());
	        }
	    }
	}

}