package com.brgenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;

import com.brgenerator.TemplateManager;
import com.brgenerator.entities.Model;
import com.brgenerator.entities.TemplateObject;
import com.brgenerator.entities.TemplateObjectAtt;
import com.brgenerator.entities.TemplateObjectNode;


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
	
	public String build(File origen, Model modelo ) throws IOException
	{
		TextAnalizer ta = new TextAnalizer(origen);
		
		//Obtengo los tipos de tag model en el archivo
		List<String> tagModels = ta.getDistinctTagModels();
		
		for (int i = 0; i < tagModels.size(); i++) 
		{
			String value = this.getTagValue(tagModels.get(i), modelo, null);
			ta.findAndReplace(tagModels.get(i), value);
		}
		
		//Obtengo las properties
		List<TemplateObject> tagProperties = ta.getElementsProperties();
		
		for (int i = 0; i < tagProperties.size(); i++) 
		{
			TemplateObjectNode ton = tagProperties.get(i).getNode(TemplateObjectNode.TypeNode.PROPERTIES);
			TemplateObjectAtt toaBegin = ton.getAttrByType(TemplateObjectAtt.TypeAtt.BEGIN);
			TemplateObjectAtt toaEnd = ton.getAttrByType(TemplateObjectAtt.TypeAtt.END);
						
			int end = (modelo.getProperties().getProperty().size()-1);
			int begin = 0;
			
			
			
			if(toaEnd != null)
			{
				//5
				int value = Integer.parseInt(toaEnd.getValue());
				
				//4
				if((modelo.getProperties().getProperty().size()-1) >= value)
				{
					end = value;
				}
			}
			if(toaBegin != null)
			{
				int value = Integer.parseInt(toaBegin.getValue());
				
				if((modelo.getProperties().getProperty().size()-1) >= value)
				{
					begin = value;
				}
			}
			
			// Ya tengo seteado el begin y el end
			
			//Ejemplos 
			// begin = 6 | end = 6 = 1
			// begin = 6 | end = 10 = 4
			
			int qPropRecorrer = (end - begin) == 0 ? 1 : (end - begin) > 0 ? end - begin : 0;
			
			
			int posicion = begin;
			
			List<TemplateObject> propertys = tagProperties.get(i).getChildElements(TemplateObjectNode.TypeNode.PROPERTY);
			
			for (int j = 0; j < propertys.size(); j++) 
			{
				TemplateObjectNode tonProperty = propertys.get(j).getNode(TemplateObjectNode.TypeNode.PROPERTY);
				TemplateObjectAtt toaPos = tonProperty.getAttrByType(TemplateObjectAtt.TypeAtt.POS);
				List<TemplateObject> attrs = propertys.get(j).getChildElements(TemplateObjectNode.TypeNode.ATTR);
				
				if(toaPos != null)
				{
					int propElem = begin + Integer.parseInt(toaPos.getValue());
					//Reemplazar valores de atts segun la posicion indicada
				}
				else
				{
					
					//Iterar por todos los properties en el rango begin --> end
					if(propertys.size()> 1)
					{
						// Vuelco info segun posicion
						
						
						//Reemplazar atributos de property
						List<TemplateObject> p = propertys.get(j).getChildElements(TemplateObjectNode.TypeNode.PROPERTY);
						
						
						//Reemplazar valores de atts en cada "posicion"
						
						
						
						posicion++;
					}
				}
				
				//Reemplazar en la property que corresponda
			}
			
			//Reemplazar en la properties que corresponda
			
			
			
		}
		
		
//		// por cada bloque de properties
//		for (int i = 0; i < tagProperties.size(); i++) 
//		{
//			TemplateObject tagPropertyFinal = new TemplateObject("",tagProperties.get(i).getFirstIndex(),tagProperties.get(i).getLastIndex());
//			
//			// Por cada prodiedad del modelo (Ej: Nombre y apellido, Altura, Edad)
//			for (int j = 0; j < modelo.getProperties().getProperty().size(); j++) 
//			{
//				//TemplateObject tagPropertyResult = new TemplateObject(tagProperties.get(i).getContent(),tagProperties.get(i).getFirstIndex(),tagProperties.get(i).getLastIndex());
//				TemplateObject tagPropertyResult = TextAnalizer.getContentTagsProperties(tagProperties.get(i)).get(0);
//				
//				// Obtengo todos los attributos de cada propiedad (Ej: Descripcion, Unique, Trim)
//				List<TemplateObject> tagPropertys = ta.getDistinctTagProperty(tagPropertyResult);
//				
//				for (int k = 0; k < tagPropertys.size(); k++) 
//				{
//					//Chequeo que la propiedad los posea
//					switch (tagPropertys.get(k).getAttr()) 
//					{
//						case "type":
//							
//							if(modelo.getProperties().getProperty().get(0).getAtts().getAtt().get(j).getType() == null)
//							{
//								//Obtengo el nodo entero desde su apertura hasta su cierre
//								List<TemplateObject> attrElements = TextAnalizer.getTagsProperty(tagPropertyResult, "type");
//								
//								for (int l = 0; l < attrElements.size(); l++) 
//								{
////									String resultado = TextAnalizer.deleteTemplateObject(tagPropertyResult, attrElements.get(l));
////									tagPropertyResult.setContent(resultado);
//									tagPropertyResult = TextAnalizer.remove(tagPropertyResult, attrElements.get(l).getContent());
//									System.out.println("Method build tagProperties.getContent(i):\n "+tagPropertyResult.getContent());
//								}
//							}
//							else
//							{
//								List<TemplateObject> allTagPropertys = TextAnalizer.getAllTagsPropertyByType(tagPropertyResult, "type");
//								
//								for (int l = 0; l < allTagPropertys.size(); l++) 
//								{
//									String valor = this.getTagValue(allTagPropertys.get(l).getContent(), null, modelo.getProperties().getProperty().get(j));
//									tagPropertyResult = TextAnalizer.findAndReplace(tagPropertyResult, allTagPropertys.get(l).getContent(), valor);
//									System.out.println("Method build tagProperties.getContent(i):\n "+tagPropertyResult.getContent());
//								}
//							}
//							
//						break;
//						case "name":
//							
//							if(modelo.getProperties().getProperty().get(j).getName() == null)
//							{
//								//Obtengo el nodo entero desde su apertura hasta su cierre
//								List<TemplateObject> attrElements = TextAnalizer.getTagsProperty(tagPropertyResult, "name");
//								
//								for (int l = 0; l < attrElements.size(); l++) 
//								{
//									tagPropertyResult = TextAnalizer.remove(tagPropertyResult, attrElements.get(l).getContent());
//									System.out.println("Method build tagProperties.getContent(i):\n "+tagPropertyResult.getContent());
//								}
//							}
//							else
//							{
//								List<TemplateObject> allTagPropertys = TextAnalizer.getAllTagsPropertyByType(tagPropertyResult, "name");
//								
//								for (int l = 0; l < allTagPropertys.size(); l++) 
//								{
//									String valor = this.getTagValue(allTagPropertys.get(l).getContent(), null, modelo.getProperties().getProperty().get(j));
//									tagPropertyResult = TextAnalizer.findAndReplace(tagPropertyResult, allTagPropertys.get(l).getContent(), valor);
//									System.out.println("Method build tagProperties.getContent(i):\n "+tagPropertyResult.getContent());
//								}
//							}
//							
//							break;
//						case "default":
//							
//							if(modelo.getProperties().getProperty().get(j).getDefault() == null)
//							{
//								//Obtengo el nodo entero desde su apertura hasta su cierre
//								List<TemplateObject> attrElements = TextAnalizer.getTagsProperty(tagPropertyResult, "default");
//								
//								for (int l = 0; l < attrElements.size(); l++) 
//								{
//									tagPropertyResult = TextAnalizer.remove(tagPropertyResult, attrElements.get(l).getContent());
//									System.out.println("Method build tagProperties.getContent(i):\n "+tagPropertyResult.getContent());
//								}
//							}
//							else
//							{
//								List<TemplateObject> allTagPropertys = TextAnalizer.getAllTagsPropertyByType(tagPropertyResult, "default");
//								
//								for (int l = 0; l < allTagPropertys.size(); l++) 
//								{
//									String valor = this.getTagValue(allTagPropertys.get(l).getContent(), null, modelo.getProperties().getProperty().get(j));
//									tagPropertyResult = TextAnalizer.findAndReplace(tagPropertyResult, allTagPropertys.get(l).getContent(), valor);
//									System.out.println("Method build tagProperties.getContent(i):\n "+tagPropertyResult.getContent());
//								}
//							}
//							
//							break;
//						case "match":
//							
//							if(modelo.getProperties().getProperty().get(j).getMatch() == null)
//							{
//								//Obtengo el nodo entero desde su apertura hasta su cierre
//								List<TemplateObject> attrElements = TextAnalizer.getTagsProperty(tagPropertyResult, "match");
//								
//								for (int l = 0; l < attrElements.size(); l++) 
//								{
//									tagPropertyResult = TextAnalizer.remove(tagPropertyResult, attrElements.get(l).getContent());
//									System.out.println("Method build tagProperties.getContent(i):\n "+tagPropertyResult.getContent());
//								}
//							}
//							else
//							{
//								List<TemplateObject> allTagPropertys = TextAnalizer.getAllTagsPropertyByType(tagPropertyResult, "match");
//								
//								for (int l = 0; l < allTagPropertys.size(); l++) 
//								{
//									String valor = this.getTagValue(allTagPropertys.get(l).getContent(), null, modelo.getProperties().getProperty().get(j));
//									tagPropertyResult = TextAnalizer.findAndReplace(tagPropertyResult, allTagPropertys.get(l).getContent(), valor);
//									System.out.println("Method build tagProperties.getContent(i):\n "+tagPropertyResult.getContent());
//								}
//							}
//							
//							break;
//						case "relation":
//							
//							if(modelo.getProperties().getProperty().get(j).getRelation() == null)
//							{
//								//Obtengo el nodo entero desde su apertura hasta su cierre
//								List<TemplateObject> attrElements = TextAnalizer.getTagsProperty(tagPropertyResult, "relation");
//								
//								for (int l = 0; l < attrElements.size(); l++) 
//								{
//									tagPropertyResult = TextAnalizer.remove(tagPropertyResult, attrElements.get(l).getContent());
//									System.out.println("Method build tagProperties.getContent(i):\n "+tagPropertyResult.getContent());
//								}
//							}
//							else
//							{
//								List<TemplateObject> allTagPropertys = TextAnalizer.getAllTagsPropertyByType(tagPropertyResult, "relation");
//								
//								for (int l = 0; l < allTagPropertys.size(); l++) 
//								{
//									String valor = this.getTagValue(allTagPropertys.get(l).getContent(), null, modelo.getProperties().getProperty().get(j));
//									tagPropertyResult = TextAnalizer.findAndReplace(tagPropertyResult, allTagPropertys.get(l).getContent(), valor);
//									System.out.println("Method build tagProperties.getContent(i):\n "+tagPropertyResult.getContent());
//								}
//							}
//							
//							break;
//						case "enum":
//							
//							if(modelo.getProperties().getProperty().get(j).getEnum() == null)
//							{
//								//Obtengo el nodo entero desde su apertura hasta su cierre
//								List<TemplateObject> attrElements = TextAnalizer.getTagsProperty(tagPropertyResult, "enum");
//								
//								for (int l = 0; l < attrElements.size(); l++) 
//								{
//									tagPropertyResult = TextAnalizer.remove(tagPropertyResult, attrElements.get(l).getContent());
//									System.out.println("Method build tagProperties.getContent(i):\n "+tagPropertyResult.getContent());
//								}
//							}
//							else
//							{
//								List<TemplateObject> allTagPropertys = TextAnalizer.getAllTagsPropertyByType(tagPropertyResult, "enum");
//								
//								for (int l = 0; l < allTagPropertys.size(); l++) 
//								{
//									String valor = this.getTagValue(allTagPropertys.get(l).getContent(), null, modelo.getProperties().getProperty().get(j));
//									tagPropertyResult = TextAnalizer.findAndReplace(tagPropertyResult, allTagPropertys.get(l).getContent(), valor);
//									System.out.println("Method build tagProperties.getContent(i):\n "+tagPropertyResult.getContent());
//								}
//							}
//							
//							break;
//						case "required":
//							
//							if(modelo.getProperties().getProperty().get(j).getRequired() == null)
//							{
//								//Obtengo el nodo entero desde su apertura hasta su cierre
//								List<TemplateObject> attrElements = TextAnalizer.getTagsProperty(tagPropertyResult, "required");
//								
//								for (int l = 0; l < attrElements.size(); l++) 
//								{
//									tagPropertyResult = TextAnalizer.remove(tagPropertyResult, attrElements.get(l).getContent());
//									System.out.println("Method build tagProperties.getContent(i):\n "+tagPropertyResult.getContent());
//								}
//							}
//							else
//							{
//								List<TemplateObject> allTagPropertys = TextAnalizer.getAllTagsPropertyByType(tagPropertyResult, "required");
//								
//								for (int l = 0; l < allTagPropertys.size(); l++) 
//								{
//									String valor = this.getTagValue(allTagPropertys.get(l).getContent(), null, modelo.getProperties().getProperty().get(j));
//									tagPropertyResult = TextAnalizer.findAndReplace(tagPropertyResult, allTagPropertys.get(l).getContent(), valor);
//									System.out.println("Method build tagProperties.getContent(i):\n "+tagPropertyResult.getContent());
//								}
//							}
//							
//							break;
//						case "trim":
//							
//							if(modelo.getProperties().getProperty().get(j).getTrim() == null)
//							{
//								//Obtengo el nodo entero desde su apertura hasta su cierre
//								List<TemplateObject> attrElements = TextAnalizer.getTagsProperty(tagPropertyResult, "trim");
//								
//								for (int l = 0; l < attrElements.size(); l++) 
//								{
//									tagPropertyResult = TextAnalizer.remove(tagPropertyResult, attrElements.get(l).getContent());
//									System.out.println("Method build tagProperties.getContent(i):\n "+tagPropertyResult.getContent());
//								}
//							}
//							else
//							{
//								List<TemplateObject> allTagPropertys = TextAnalizer.getAllTagsPropertyByType(tagPropertyResult, "trim");
//								
//								for (int l = 0; l < allTagPropertys.size(); l++) 
//								{
//									String valor = this.getTagValue(allTagPropertys.get(l).getContent(), null, modelo.getProperties().getProperty().get(j));
//									tagPropertyResult = TextAnalizer.findAndReplace(tagPropertyResult, allTagPropertys.get(l).getContent(), valor);
//									System.out.println("Method build tagProperties.getContent(i):\n "+tagPropertyResult.getContent());
//								}
//							}
//							
//							break;
//						case "unique":
//							
//							if(modelo.getProperties().getProperty().get(j).getUnique() == null)
//							{
//								//Obtengo el nodo entero desde su apertura hasta su cierre
//								List<TemplateObject> attrElements = TextAnalizer.getTagsProperty(tagPropertyResult, "unique");
//								
//								for (int l = 0; l < attrElements.size(); l++) 
//								{
//									tagPropertyResult = TextAnalizer.remove(tagPropertyResult, attrElements.get(l).getContent());
//									System.out.println("Method build tagProperties.getContent(i):\n "+tagPropertyResult.getContent());
//								}
//							}
//							else
//							{
//								List<TemplateObject> allTagPropertys = TextAnalizer.getAllTagsPropertyByType(tagPropertyResult, "unique");
//								
//								for (int l = 0; l < allTagPropertys.size(); l++) 
//								{
//									String valor = this.getTagValue(allTagPropertys.get(l).getContent(), null, modelo.getProperties().getProperty().get(j));
//									tagPropertyResult = TextAnalizer.findAndReplace(tagPropertyResult, allTagPropertys.get(l).getContent(), valor);
//									System.out.println("Method build tagProperties.getContent(i):\n "+tagPropertyResult.getContent());
//								}
//							}
//							
//							break;
//						case "validate":
//							
//							if(modelo.getProperties().getProperty().get(j).getValidate() == null)
//							{
//								//Obtengo el nodo entero desde su apertura hasta su cierre
//								List<TemplateObject> attrElements = TextAnalizer.getTagsProperty(tagPropertyResult, "validate");
//								
//								for (int l = 0; l < attrElements.size(); l++) 
//								{
//									tagPropertyResult = TextAnalizer.remove(tagPropertyResult, attrElements.get(l).getContent());
//									System.out.println("Method build tagProperties.getContent(i):\n "+tagPropertyResult.getContent());
//								}
//							}
//							else
//							{
//								List<TemplateObject> allTagPropertys = TextAnalizer.getAllTagsPropertyByType(tagPropertyResult, "validate");
//								
//								for (int l = 0; l < allTagPropertys.size(); l++) 
//								{
//									String valor = this.getTagValue(allTagPropertys.get(l).getContent(), null, modelo.getProperties().getProperty().get(j));
//									tagPropertyResult = TextAnalizer.findAndReplace(tagPropertyResult, allTagPropertys.get(l).getContent(), valor);
//									System.out.println("Method build tagProperties.getContent(i):\n "+tagPropertyResult.getContent());
//								}
//							}
//							
//							break;
//						default:
//							break;
//				}
//				
//			}
//
//				tagPropertyResult = TextAnalizer.setCuotes(tagPropertyResult,0);
//				tagPropertyFinal.appendContent(tagPropertyResult.getContent());
//		}
//			
//			tagPropertyFinal = TextAnalizer.setCuotes(tagPropertyFinal,1);
//			ta.replace(tagPropertyFinal);
//		}
		
		return ta.content.getContent();
	}
	
	public String getTagValue(String tag, Model model, Model.Properties.Property property)
	{
		Inflector inf = new Inflector();
		tag = tag.substring(1, tag.length()-1);
		String[] values = tag.split(":");
		String tagAux = "";
		
		switch (values[0]) 
		{
			case "model":
				
				for (int i = 0; i < values.length; i++) 
				{
					switch (values[i]) 
					{
						case "name":
							tagAux = model.getName();
							break;
						case "lower":
							tagAux = tagAux.toLowerCase();
							break;
						case "plural":
							tagAux = inf.pluralize(tagAux);
							break;
						default:
							break;
					}
				}
				
				break;
				
			case "property":
				
				if(values.length <=2)
				{
					return "";
				}
				else
				{
					for (int i = 2; i < values.length; i++) 
					{
						switch (values[i]) 
						{
								case "value":
									
									switch (values[i-1]) 
									{
										case "name":
											tagAux = property.getName();
											break;
										case "type":
											tagAux = property.getType().toString();
											break;
										case "default":
											tagAux = property.getDefault().getValue();
											break;
										case "match":
											tagAux = property.getMatch().getValue();
											break;
										case "relation":
											tagAux = property.getRelation().getValue();
											break;
										case "enum":
											tagAux = property.getEnum().getValue();
											break;
										case "required":
											tagAux = property.getRequired().getValue();
											break;
										case "trim":
											tagAux = property.getTrim().getValue().toString();
											break;
										case "unique":
											tagAux = property.getUnique().getValue();
											break;
										case "validate":
											tagAux = property.getValidate().getValue();
											break;
										case "lower":
											tagAux = tagAux.toLowerCase();
											break;
										case "plural":
											tagAux = inf.pluralize(tagAux);
											break;
										default:
											break;
									}
									
							case "lower":
								tagAux = tagAux.toLowerCase();
								break;
							case "plural":
								tagAux = inf.pluralize(tagAux);
								break;
							default:
								break;
						}
					}	
				}
				
				break;
				
			default:
				break;
		}
		
		
		return tagAux;
		
	}

}