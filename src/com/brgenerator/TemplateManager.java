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
						
						while (!propertyAux.getNodesByType(TypeNode.PROPERTY).isEmpty()) 
						{
							TemplateObjectNode nodo = propertyAux.getNodesByType(TypeNode.PROPERTY).get(0);
							String valor = this.getValue(TypeNode.PROPERTY, nodo, modelo.getProperties().getProperty().get(j));
							nodo.setContent(valor);
							propertyAux.replace(nodo);
						}
						
						propertyAll.append(propertyAux);
						
					}
					
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
								
								while (!propertyAux.getNodesByType(TypeNode.PROPERTY).isEmpty()) 
								{
									TemplateObjectNode nodo = propertyAux.getNodesByType(TypeNode.PROPERTY).get(0);
									String valor = this.getValue(TypeNode.PROPERTY, nodo, modelo.getProperties().getProperty().get(posicion));
									nodo.setContent(valor);
									propertyAux.replace(nodo);
								}
								
								tagPropertiesAux.replace(propertyAux);
								
								posicion++;
							}
							else
							{
								TemplateObject propertyAux = new TemplateObject("",tagPropertiesAux.getChildElements(TemplateObjectNode.TypeNode.PROPERTY).get(0).getFirstIndex(),tagPropertiesAux.getChildElements(TemplateObjectNode.TypeNode.PROPERTY).get(0).getLastIndex(),Type.TEXT);
								tagPropertiesAux.replace(propertyAux);
							}
						}
						
						
//						for (int i = 0; i < tagPropertysAux.size(); i++) 
//						{
//							if(posicion <= end)
//							{
//								TemplateObject propertyAux = new TemplateObject(tagPropertysAux.get(i));
//								
//								while (!propertyAux.getNodesByType(TypeNode.PROPERTY).isEmpty()) 
//								{
//									TemplateObjectNode nodo = propertyAux.getNodesByType(TypeNode.PROPERTY).get(0);
//									String valor = this.getValue(TypeNode.PROPERTY, nodo, modelo.getProperties().getProperty().get(posicion));
//									nodo.setContent(valor);
//									propertyAux.replace(nodo);
//								}
//								
//								tagPropertiesAux.replace(propertyAux);
//								
//								posicion++;
//							}
//							else
//							{
////								TemplateObject propertyAux = new TemplateObject("NADA",tagPropertysAux.get(i).getFirstIndex(),tagPropertysAux.get(i).getLastIndex(),Type.PROPERTY);
////								tagPropertiesAux.replace(propertyAux);
//							}
//						}
						
						tagPropertiesAll.append(tagPropertiesAux);
					}
					
					to.replace(tagPropertiesAll);
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
						result = tonAttMode.getValue().equalsIgnoreCase("plural")?  inf.pluralize(result):result;
					}
					if(tonAttCase != null)
					{
						result = tonAttCase.getValue().equalsIgnoreCase("lower")?  result.toLowerCase():result;
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
							result = propiedad.getName();
							break;
						case "label":
							result = propiedad.getLabel();
							break;
						default:
							break;
					}
					
					if(tonAttMode != null)
					{
						result = tonAttMode.getValue().equalsIgnoreCase("plural")?  inf.pluralize(result):result;
					}
					if(tonAttCase != null)
					{
						result = tonAttCase.getValue().equalsIgnoreCase("lower")?  result.toLowerCase():result;
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