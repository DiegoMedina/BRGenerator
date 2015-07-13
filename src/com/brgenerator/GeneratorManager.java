package com.brgenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.brgenerator.entities.Model;
import com.brgenerator.entities.Model.Properties;

import javax.servlet.ServletContext;


public class GeneratorManager
{
	
	private List<Model> modelos = new ArrayList<Model>();
	private File forigen;
	private File fdestino;
	private File fmodelos;
	
	public GeneratorManager(File f_origen, File f_destino, File f_modelos)
	{
	    forigen = f_origen;
	    fdestino = f_destino;
	    fmodelos = f_modelos;
	}
	
	public void loadModels()
	{
		modelos.clear();
		System.out.println("Carpeta con modelos: " + fmodelos);
		
		//make sure source exists
    	if(!fmodelos.exists())
    	{
    		System.out.println("Directory Model does not exist.");
           //just exit
           System.exit(0);
        }
    	else
    	{
    	    String files[] = fmodelos.list();
    	    
       		for (String file : files) 
       		{
       			File srcFile = new File(fmodelos, file);
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
		System.out.println("Carpeta con recursos: " + forigen);
		

    	//make sure source exists
    	if(!forigen.exists())
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
        	   generate(forigen,fdestino);
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
		
		TemplateManager tm = new TemplateManager(this.modelos);
		
    	if(src.isDirectory())
    	{
    		//Si la carpeta no existe, se crea
    		
    		if(!dest.exists())
    		{
    		   dest.mkdir();
    		   System.out.println("Carpeta copiada de " + src + " a " + dest);
    		}
 
    		//Se obtiene todo el contenido de el directorio
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
    		if(src.getName().endsWith(TemplateManager.TEMPLATE_MODEL_EXTENSION))
    		{
    			tm.build(src, dest);
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

}