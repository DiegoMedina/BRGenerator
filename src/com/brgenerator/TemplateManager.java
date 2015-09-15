package com.brgenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import com.brgenerator.TemplateManager;
import com.brgenerator.entities.Model;
import com.brgenerator.entities.TemplateObject;
import com.brgenerator.entities.TemplateObject.Type;
import com.brgenerator.entities.TemplateObjectAtt;
import com.brgenerator.entities.TemplateObjectNode;
import com.brgenerator.entities.TemplateObjectAtt.TypeAtt;
import com.brgenerator.entities.TemplateObjectNode.TypeNode;


public class TemplateManager {
	
	static File[] files;
	static String pathRoot = "";
	static String[] pathTemplates;
	public static final String DOT_SEPARATOR = ".";
	public static final String TEMPLATE_EXTENSION = ".brt";
	public static final String TEMPLATE_MODEL_EXTENSION = ".brm";
	public static final String BIGRUNTS_DIRECTORY_TEMPLATE = ".brd";
	public static final String BIGRUNTS_FILE_TEMPLATE = ".brf";
	public static final String XML_EXTENSION= ".xml";
	public static final String DEPRECATED = ".deprec";
	private List<Model> modelos;
	
	public TemplateManager(List<Model> modelos)
	{
		this.modelos = modelos;
	}
	
	public static void main(String path)
	{
		pathRoot = path;
		files = new File(path).listFiles();
	}
	
	public Boolean isDirectoryTemplate(String folderName)
	{
		return folderName.endsWith(TemplateManager.BIGRUNTS_DIRECTORY_TEMPLATE);
	}
	
	public Boolean isFileTemplate(String fileName)
	{
		return fileName.endsWith(TemplateManager.BIGRUNTS_FILE_TEMPLATE);
	}
	
	public Boolean isDeprecated(String fileName)
	{
		return fileName.endsWith(TemplateManager.DEPRECATED);
	}
	
	public String getFinalPath(String path, Model modelo)
	{
		String aux = "";
		Inflector inf = new Inflector();
		aux = path.replace("xxxs", inf.pluralize(modelo.getName()).toLowerCase());
		aux = aux.replace("xxx", modelo.getName().toLowerCase());
		

		aux = aux.replace(TemplateManager.BIGRUNTS_DIRECTORY_TEMPLATE, "");
		aux = aux.replace(TemplateManager.BIGRUNTS_FILE_TEMPLATE, "");
		
		System.out.println("Method getFinalPath: " + "aux " + aux);
		
		return aux;
	}
	
	public void manageTemplate(File origen, File destino, Model belonModel) throws IOException
	{
		if(!this.isDeprecated(origen.getName()))
		{
			if(origen.isDirectory())
	    	{
	    		//Si la carpeta no existe, se crea
				
				if(this.isDirectoryTemplate(origen.getName()))
				{
					for (int i = 0; i < modelos.size(); i++) 
					{
						File destTemp = new File(this.getFinalPath(destino.getAbsolutePath(), modelos.get(i)));
						
						if(!destTemp.exists())
			    		{
							destTemp.mkdir();
			    			System.out.println("Method manageTemplate: " + "destTemp " + destTemp);
			    		}
						
						//Se obtiene todo el contenido de el directorio
			    		String files[] = origen.list();
			 
			    		for (String file : files)
			    		{
			    		   //construct the src and dest file structure
			    		   File srcFile = new File(origen, file);
			    		   File destFile = new File(destTemp, file);
			    		   //recursive copy
			    		   manageTemplate(srcFile,destFile, modelos.get(i));
			    		}
						
					}
				}
				else
				{
					if(!destino.exists())
		    		{
		    		   destino.mkdir();
		    		   System.out.println("Method manageTemplate: " + "aux " + destino);
		    		}
					
					//Se obtiene todo el contenido de el directorio
		    		String files[] = origen.list();
		 
		    		for (String file : files)
		    		{
		    		   //construct the src and dest file structure
		    		   File srcFile = new File(origen, file);
		    		   File destFile = new File(destino, file);
		    		   //recursive copy
		    		   manageTemplate(srcFile,destFile, belonModel);
		    		}
				}
				
	    	}
	    	else
	    	{
	    		
	    		if(this.isFileTemplate(origen.getName()))
	    		{
	    			
	    			if(belonModel == null)
	    			{
		    			for (int i = 0; i < modelos.size(); i++) 
						{
		    				String path = getFinalPath(destino.getPath(), modelos.get(i));	
		    				File btm = new File(path);
		    				BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(btm), "UTF-8"));
		    				fileWriter.write(this.build(origen, modelos.get(i)));
		    				fileWriter.flush();
		    				fileWriter.close();
		    				
		         	        System.out.println("Method manageTemplate loop ("+i+")" + modelos.get(i).getName() + ": " + " origen " + origen + " destino " + destino);
		    				
						}
	    			}
	    			else
	    			{
				
	    				String path = getFinalPath(destino.getPath(), belonModel);	
	    				File btm = new File(path);
	    				BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(btm), "UTF-8"));
	    				fileWriter.write(this.build(origen, belonModel));
	    				fileWriter.flush();
	    				fileWriter.close();
	    				
	    				System.out.println("Method manageTemplate model ("+belonModel.getName()+") : " + " origen " + origen + " destino " + destino);
	    			}
	    			
				}
	
	    	}
		}
	}
	
	public TemplateObject buildModel(TemplateObject to, Model modelo)
	{
		
		while (!to.getNodesByType(TypeNode.MODEL).isEmpty()) 
		{
			TemplateObjectNode nodo = to.getNodesByType(TypeNode.MODEL).get(0);
			String valor = this.getValue(TypeNode.MODEL, nodo, modelo);
			nodo.setContent(valor);
			to.replace(nodo);
		}
		
		return to;
	}
	
	public TemplateObject buildProperties(TemplateObject to, Model modelo)
	{
		//Obtengo las properties
		List<TemplateObject> tagProperties = to.getChildElements(TemplateObjectNode.TypeNode.PROPERTIES);
		
		for (int i = 0; i < tagProperties.size(); i++) 
		{
			TemplateObjectNode ton = tagProperties.get(i).getNode();
			TemplateObjectAtt toaBegin = ton.getAttrByType(TemplateObjectAtt.TypeAtt.BEGIN);
			TemplateObjectAtt toaEnd = ton.getAttrByType(TemplateObjectAtt.TypeAtt.END);
						
			//VALOR POR DEFAULT PARA END EL TOTAL DE LA CANTIDAD DE PROPIEDADES/CAMPOS DEL MODELO
			int end = (modelo.getProperties().getProperty().size()-1);
			//VALOR POR DEFAULT PARA EL COMIENZO 0
			int begin = 0;

			
			if(toaEnd != null)
			{
				//VALUE = VALOR DE END EN NODO PROPERTIES DEL TEMPLATE
				int value = Integer.parseInt(toaEnd.getValue());
				
				//SI EL VALOR DEL NODO ES MENOR QUE EL TOTAL DE PROPIEDADES/CAMPOS EN EL MODELO
				if((modelo.getProperties().getProperty().size()-1) >= value)
				{
					// END = VALOR EN EL NODO PROPERTIES DEL TEMPLATE
					end = value;
				}
			}
			if(toaBegin != null)
			{
				//VALUE = VALOR DE BEGIN EN NODO PROPERTIES DEL TEMPLATE
				int value = Integer.parseInt(toaBegin.getValue());
				
				//SI EL VALOR DEL NODO ES MENOR QUE EL TOTAL DE PROPIEDADES/CAMPOS EN EL MODELO Y MAYOR QUE 0
				if((value <= modelo.getProperties().getProperty().size()-1) && value > 0)
				{
					// BEGIN = VALOR EN EL NODO PROPERTIES DEL TEMPLATE
					begin = value;
				}
			}
			
			
			List<TemplateObject> tagPropertys = tagProperties.get(i).getChildElements(TemplateObjectNode.TypeNode.PROPERTY);
			
			if(tagPropertys.size() > 0)
			{
				if(tagPropertys.size() == 1)
				{
					for (int j = begin; j <= end; j++) 
					{
						//OBTENGO ELEMENTOS HIJOS DE TIPO ATT
						List<TemplateObject> tagAtts = tagPropertys.get(0).getChildElements(TemplateObjectNode.TypeNode.ATT);
						
						if(tagAtts.size() > 0)
						{
							for (int k = 0; k < tagAtts.size(); k++) 
							{
								TemplateObject toatt = tagAtts.get(k);
								//toatt = <ATT>alaslallsals</ATT>
								toatt = buildAtt(toatt, modelo.getProperties().getProperty().get(j).getAtts());
								tagAtts.get(k).replace(toatt);
							}
							
						}
						
					}
					
				}
			}
		}
		
		return to;
	}
	
	public TemplateObject buildProperty(TemplateObject to, Model modelo)
	{
		
		
		
		// SI TIENE UN SOLO ELEMENTO PROPERTY ITERO POR CADA PROPIEDAD/CAMPO EN EL RANGO INDICADO
		
		
		return to;
	}
	
	public TemplateObject buildAtt(TemplateObject to, Model.Properties.Property.Atts atributos)
	{
		
		TemplateObjectNode ton = to.getNode();
		TemplateObjectAtt toaBegin = ton.getAttrByType(TemplateObjectAtt.TypeAtt.BEGIN);
		TemplateObjectAtt toaEnd = ton.getAttrByType(TemplateObjectAtt.TypeAtt.END);
					
		//VALOR POR DEFAULT PARA END EL TOTAL DE LA CANTIDAD DE PROPIEDADES/CAMPOS DEL MODELO
		int end = atributos.getAtt().size() -1;
		//VALOR POR DEFAULT PARA EL COMIENZO 0
		int begin = 0;

		
		if(toaEnd != null)
		{
			//VALUE = VALOR DE END EN NODO PROPERTIES DEL TEMPLATE
			int value = Integer.parseInt(toaEnd.getValue());
			
			//SI EL VALOR DEL NODO ES MENOR QUE EL TOTAL DE PROPIEDADES/CAMPOS EN EL MODELO
			if((atributos.getAtt().size() -1) >= value)
			{
				// END = VALOR EN EL NODO PROPERTIES DEL TEMPLATE
				end = value;
			}
		}
		if(toaBegin != null)
		{
			//VALUE = VALOR DE BEGIN EN NODO PROPERTIES DEL TEMPLATE
			int value = Integer.parseInt(toaBegin.getValue());
			
			//SI EL VALOR DEL NODO ES MENOR QUE EL TOTAL DE PROPIEDADES/CAMPOS EN EL MODELO Y MAYOR QUE 0
			if((value <= atributos.getAtt().size() -1) && value > 0)
			{
				// BEGIN = VALOR EN EL NODO PROPERTIES DEL TEMPLATE
				begin = value;
			}
		}
		
		TemplateObject attFinal = new TemplateObject("", 0, 0, Type.ATT);
		
		for (int i = begin; i <= end; i++) 
		{	
			TemplateObject attNuevo = new TemplateObject(to);
			
			//MIENTRAS ENCUENTRE NODOS TAG DENTRO REEMPLAZO
			while (!attNuevo.getNodesByType(TypeNode.ATT).isEmpty()) 
			{
				TemplateObjectNode nodo = attNuevo.getNodesByType(TypeNode.ATT).get(0);
				String valor = this.getValue(TypeNode.ATT, nodo, atributos.getAtt().get(i));
				nodo.setContent(valor);
				attNuevo.replace(nodo);
			}
			
			attFinal.append(attNuevo);
		}
		
		to.setContent(attFinal.getContent());
		
		return to;
	}
	
	public String build(File origen, Model modelo ) throws IOException
	{
		
		TemplateObject to = new TemplateObject(origen);
		
		// CONSTRUYO MODEL
		to = buildModel(to, modelo);
		
		to = buildProperties(to, modelo);
		
		
		return to.getContent();
	}
	
	public String getValue(TemplateObjectNode.TypeNode nodeType, TemplateObjectNode nodo, Object entity)
	{
		Inflector inf = new Inflector();
		String result = "";
		
		TemplateObjectAtt tonAttValue;
		TemplateObjectAtt tonAttMode;
		TemplateObjectAtt tonAttCase;
		
		switch (nodeType) 
		{
			case MODEL:
				
				Model modelo = (Model)entity;
				tonAttValue = nodo.getAttrByType(TypeAtt.VALUE);
				tonAttMode = nodo.getAttrByType(TypeAtt.MODE);
				tonAttCase = nodo.getAttrByType(TypeAtt.CASE);
				
				if(tonAttValue != null)
				{
					switch (tonAttValue.getValue()) 
					{
						case "name":
							result = modelo.getName();
							break;
						default:
							break;
					}
					
					if(tonAttMode != null)
					{
						result = tonAttMode.getValue() == "plural"?  inf.pluralize(result):result;
					}
					if(tonAttCase != null)
					{
						result = tonAttCase.getValue() == "lower"?  result.toLowerCase():result;
					}
				}
				
			break;
			case ATT:
				
				Model.Properties.Property property = (Model.Properties.Property)entity;
				tonAttValue = nodo.getAttrByType(TypeAtt.VALUE);
				tonAttMode = nodo.getAttrByType(TypeAtt.MODE);
				tonAttCase = nodo.getAttrByType(TypeAtt.CASE);
				
				if(tonAttValue != null)
				{
					for (int i = 0; i < property.getAtts().getAtt().size(); i++) 
					{
						if(property.getAtts().getAtt().get(i).getName() == tonAttValue.getValue())
						{
							result = property.getAtts().getAtt().get(i).getValue();
						}
					}
					
					if(tonAttMode != null)
					{
						result = tonAttMode.getValue() == "plural"?  inf.pluralize(result):result;
					}
					if(tonAttCase != null)
					{
						result = tonAttCase.getValue() == "lower"?  result.toLowerCase():result;
					}
				}
			break;
			default:
				break;
		}

		return result;
		
	}

}