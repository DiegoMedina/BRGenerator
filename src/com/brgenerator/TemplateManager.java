package com.brgenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import com.brgenerator.TemplateManager;
import com.brgenerator.entities.Model;
import com.brgenerator.entities.Model.Properties.Property;
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
		
		while (!to.getChildElements(TemplateObjectNode.TypeNode.PROPERTIES).isEmpty()) 
		{
			
			TemplateObject tagProperties = to.getChildElements(TemplateObjectNode.TypeNode.PROPERTIES).get(0);
			
			TemplateObjectNode ton = tagProperties.getNode();
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
			
			
			List<TemplateObject> tagPropertys = tagProperties.getChildElements(TemplateObjectNode.TypeNode.PROPERTY);
			
			if(tagPropertys.size() > 0)
			{
				if(tagPropertys.size() == 1)
				{
					TemplateObject propertyAll = new TemplateObject("", tagPropertys.get(0).getFirstIndex(), tagPropertys.get(0).getLastIndex(), Type.PROPERTY);
					
					for (int j = begin; j <= end; j++) 
					{
						TemplateObject propertyAux = new TemplateObject(tagPropertys.get(0));
						
						propertyAux = this.buildProperty(propertyAux, modelo.getProperties().getProperty().get(j));
						
						propertyAll.append(propertyAux);
						
					}
					
					propertyAll = buildCuotes(propertyAll);
					
					tagProperties.replace(propertyAll);
					
					to.replace(tagProperties);
					
				}
				else if(tagPropertys.size() > 1)
				{
					
					//CREO UNA COPIA DEL PROPERTIES CONTENEDORA A LA QUE LE HARE UN APPEND AL FINAL DE CADA WHILE
					TemplateObject tagPropertiesAll = new TemplateObject("", tagProperties.getFirstIndex(),tagProperties.getLastIndex(),Type.PROPERTIES);
					
					int posicion = begin;
					
					while (posicion <= end) 
					{
						//CREO UNA COPIA DEL PROPERTIES CONTENEDORA A LA QUE LE REEMPLAZARE LOS NODOS PROPERTYES
						TemplateObject tagPropertiesAux = new TemplateObject(tagProperties);
						
						while (!tagPropertiesAux.getChildElements(TemplateObjectNode.TypeNode.PROPERTY).isEmpty()) 
						{
							if(posicion <= end)
							{
								TemplateObject propertyAux = new TemplateObject(tagPropertiesAux.getChildElements(TemplateObjectNode.TypeNode.PROPERTY).get(0));
								
								propertyAux = this.buildProperty(propertyAux, modelo.getProperties().getProperty().get(posicion));
								
								tagPropertiesAux.replace(propertyAux);
								
								posicion++;
							}
							else
							{
								TemplateObject propertyAux = new TemplateObject("",tagPropertiesAux.getChildElements(TemplateObjectNode.TypeNode.PROPERTY).get(0).getFirstIndex(),tagPropertiesAux.getChildElements(TemplateObjectNode.TypeNode.PROPERTY).get(0).getLastIndex(),Type.TEXT);
								tagPropertiesAux.replace(propertyAux);
							}
						}
						
						tagPropertiesAll.append(tagPropertiesAux);
					}
					
					tagPropertiesAll = buildCuotes(tagPropertiesAll);
					
					to.replace(tagPropertiesAll);
				}
			}	
		}
		
		return to;
	}
	
	public TemplateObject buildProperty(TemplateObject to, Property property)
	{
		while (!to.getNodesByType(TypeNode.PROPERTY).isEmpty()) 
		{
			TemplateObjectNode nodo = to.getNodesByType(TypeNode.PROPERTY).get(0);
			String valor = this.getValue(TypeNode.PROPERTY, nodo, property);
			nodo.setContent(valor);
			to.replace(nodo);
		}
		
		List<TemplateObject> tagAtts = to.getChildElements(TemplateObjectNode.TypeNode.ATT);
		
		if(tagAtts.size() > 0)
		{
			if(tagAtts.size() == 1)
			{
				TemplateObject attAll = new TemplateObject("", tagAtts.get(0).getFirstIndex(), tagAtts.get(0).getLastIndex(), Type.ATT);
				
				for (int i = 0; i < property.getAtts().getAtt().size(); i++) 
				{
					TemplateObject attAux = new TemplateObject(tagAtts.get(0));
					attAux = this.buildAtt(attAux, property.getAtts().getAtt().get(i));
					attAll.append(attAux);
				}
				
				attAll = buildCuotes(attAll);
				
				to.replace(attAll);
				
			}
		}
		
		while (!to.getNodesByType(TypeNode.ATT).isEmpty()) 
		{
			TemplateObjectNode nodo = to.getNodesByType(TypeNode.ATT).get(0);
			String valor = this.getValue(TypeNode.ATT, nodo, property);
			nodo.setContent(valor);
			to.replace(nodo);
		}
	
		return to;
	}
	
	public TemplateObject buildCuotes(TemplateObject to)
	{
		while (!to.getNodesByType(TypeNode.CUOTE).isEmpty()) 
		{
			TemplateObjectNode nodo = to.getNodesByType(TypeNode.CUOTE).get(0);
			String valor = this.getValue(TypeNode.CUOTE, nodo, (to.getNodesByType(TypeNode.CUOTE).size()==1?"":","));
			nodo.setContent(valor);
			to.replace(nodo);
		}
		
		return to;
	}
	
	public TemplateObject buildAtt(TemplateObject to, Model.Properties.Property.Atts.Att atributo)
	{
		while (!to.getNodesByType(TypeNode.ATT).isEmpty()) 
		{
			TemplateObjectNode nodo = to.getNodesByType(TypeNode.ATT).get(0);
			String valor = this.getValue(TypeNode.ATT, nodo, atributo);
			nodo.setContent(valor);
			to.replace(nodo);
		}
		
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
							result = modelo.getName().trim();
							break;
						default:
							break;
					}
					
					if(tonAttMode != null)
					{
						result = tonAttMode.getValue().trim().equalsIgnoreCase("plural")?  inf.pluralize(result):result;
					}
					if(tonAttCase != null)
					{
						result = tonAttCase.getValue().trim().equalsIgnoreCase("lower")?  result.toLowerCase():result;
					}
				}
				
			break;
			case PROPERTY:
				
				Property propiedad = (Property)entity;
				tonAttValue = nodo.getAttrByType(TypeAtt.VALUE);
				tonAttMode = nodo.getAttrByType(TypeAtt.MODE);
				tonAttCase = nodo.getAttrByType(TypeAtt.CASE);
				
				if(tonAttValue != null)
				{
					switch (tonAttValue.getValue()) 
					{
						case "name":
							result = propiedad.getName().trim();
							break;
						case "label":
							result = propiedad.getLabel().trim();
							break;
						default:
							break;
					}
					
					if(tonAttMode != null)
					{
						result = tonAttMode.getValue().trim().equalsIgnoreCase("plural")?  inf.pluralize(result):result;
					}
					if(tonAttCase != null)
					{
						result = tonAttCase.getValue().trim().equalsIgnoreCase("lower")?  result.toLowerCase():result;
					}
				}
				
			break;
			case ATT:
				 
				tonAttValue = nodo.getAttrByType(TypeAtt.VALUE);
				tonAttMode = nodo.getAttrByType(TypeAtt.MODE);
				tonAttCase = nodo.getAttrByType(TypeAtt.CASE);
				
				if(tonAttValue != null)
				{
					
					if (entity instanceof Model.Properties.Property.Atts.Att) 
					{
						Model.Properties.Property.Atts.Att attribute = (Model.Properties.Property.Atts.Att)entity;
						
						switch (tonAttValue.getValue()) 
						{
							case "name":
								result = attribute.getName().trim();
								break;
							case "value":
								result = attribute.getValue().trim();
								break;
							default:
								break;
						}
					}
					else if (entity instanceof Model.Properties.Property) 
					{
						Model.Properties.Property property = (Model.Properties.Property)entity;
						
						for (int i = 0; i < property.getAtts().getAtt().size(); i++) 
						{
							if(property.getAtts().getAtt().get(i).getName().trim().equalsIgnoreCase(tonAttValue.getValue().trim()))
							{
								result = property.getAtts().getAtt().get(i).getValue().trim();
							}
						}
					}
					
					if(tonAttMode != null)
					{
						result = tonAttMode.getValue().trim().equalsIgnoreCase("plural")?  inf.pluralize(result):result;
					}
					if(tonAttCase != null)
					{
						result = tonAttCase.getValue().trim().equalsIgnoreCase("lower")?  result.toLowerCase():result;
					}
				}
			break;
			case CUOTE: 
			{
				result = (String)entity;
			}
			break;
			default:
				break;
		}

		return result;
		
	}

}