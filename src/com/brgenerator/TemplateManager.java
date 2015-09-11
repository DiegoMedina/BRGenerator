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
import com.brgenerator.entities.Model.Properties;
import com.brgenerator.entities.Model.Properties.Property;
import com.brgenerator.entities.TemplateObject;
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
	
	public String build(File origen, Model modelo ) throws IOException
	{
		
		TemplateObject to = new TemplateObject(origen);
		
		List<TemplateObjectNode> tagModels = to.getNodesByType(TypeNode.MODEL);
		
		while (!to.getNodesByType(TypeNode.MODEL).isEmpty()) 
		{
			TemplateObjectNode nodo = to.getNodesByType(TypeNode.MODEL).get(0);
			String valor = this.getValue(TypeNode.MODEL, nodo, modelo);
			nodo.setContent(valor);
			to.replace(nodo);
		}
		
		
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
			
			// SI TIENE UN SOLO ELEMENTO PROPERTY ITERO POR CADA PROPIEDAD/CAMPO EN EL RANGO INDICADO
			if(tagPropertys.size() == 1)
			{
				//LISTADO DE PROPERTYS RESULTADO
				List<TemplateObject> propertysRes;
								
				for (int j = begin; j <= end; j++) 
				{
					//CREO UN NUEVO TAG PROPERTY
					TemplateObject propertysAux = new TemplateObject(tagPropertys.get(0));
					
					//OBTENGO ELEMENTOS HIJOS DE TIPO ATT
					List<TemplateObject> tagAtts = tagPropertys.get(0).getChildElements(TemplateObjectNode.TypeNode.ATT);
					
					//SI TENGO ELEMENTOS TAG
					if(tagAtts.size() > 0)
					{
						//LISTADO DE ATTS RESULTADO
						List<TemplateObject> attsRes;
						
						// ITERO POR CADA ELEMENTO TAG
						for (int k = 0; k < tagAtts.size(); k++) 
						{	
							//CREO UN NUEVO TAG ATT
							TemplateObject attAux = new TemplateObject(tagAtts.get(k));
							
							//MIENTRAS ENCUENTRE NODOS TAG DENTRO REEMPLAZO
							while (!tagAtts.get(k).getNodesByType(TypeNode.ATT).isEmpty()) 
							{
								TemplateObjectNode nodo = tagAtts.get(k).getNodesByType(TypeNode.ATT).get(0);
								String valor = this.getValue(TypeNode.ATT, nodo, modelo.getProperties().getProperty().get(j));
								nodo.setContent(valor);
								to.replace(nodo);
							}
							
							// YA REEMPLACE TODOS LOS NODOS TAG
							// AGREGO ATT A ATTS
							attsRes.add(attAux);
						}
						
						
					}
					else 
					{
						while (!tagPropertys.get(j).getNodesByType(TypeNode.ATT).isEmpty()) 
						{
							TemplateObjectNode nodo = tagAtts.get(k).getNodesByType(TypeNode.ATT).get(0);
							String valor = this.getValue(TypeNode.ATT, nodo, modelo.getProperties().getProperty().get(j));
							nodo.setContent(valor);
							to.replace(nodo);
						}
						
					}
					
				}
			}
			else
			{
				
			}
			
			int qIteraciones = 0;
			
			for (int j = begin; j <= end; j++) 
			{
				//OBTENGO TODAS LAS PROPERTYS DENTRO DEL NODO
				
				
				//OBTENGO LA PROPIEDAD/CAMPO QUE CORRESPONDE DEL MODELO 
				Property propy = modelo.getProperties().getProperty().get(j);
				
				for (int k = 0; k < tagPropertys.size(); k++) 
				{
					
				}
				
				qIteraciones++;
			}
			
			int qPropRecorrer = (end - begin) == 0 ? 1 : (end - begin) > 0 ? end - begin : 0;
			int posicion = begin;
			
			

			if(propertys.size() == 1)
			{
				
				for (int j = 0; j < modelo.getProperties().getProperty().size(); j++) 
				{
					Properties.Property propiedad = modelo.getProperties().getProperty().get(j);
					
					List<TemplateObject> propertysChilds = propertys.get(0).getChildElements(TemplateObjectNode.TypeNode.PROPERTY);
					
					for (int k = 0; k < propertysChilds.size(); k++) 
					{
						//Obtengo nodo
						TemplateObjectNode tonpc = propertysChilds.get(k).getNode(TemplateObjectNode.TypeNode.PROPERTY);
						//Obtengo att value
						TemplateObjectAtt tonpcAtt = tonpc.getAttrByType(TemplateObjectAtt.TypeAtt.VALUE);
						String valor = "";
						
						switch (tonpcAtt.getKey()) {
						case "name":
							valor = propiedad.getName();
							break;
						case "label":
							valor = propiedad.getLabel();
							break;
						default:
							break;
						}
						
						//REEMPLAZAR NODO POR "valor"
						
					}
					
				}
				
				//Obtener nodos property y reemplazar segun value
				
				
				//Obtener nodos att y reemplazar segun value
				
			}
			else
			{
				
				
			}

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