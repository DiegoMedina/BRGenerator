package com.brgenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.brgenerator.TemplateManager;
import com.brgenerator.entities.Model;
import com.brgenerator.entities.ObjectFactory;


public class TemplateManager {
	
	static File[] files;
	static String pathRoot = "";
	static String[] pathTemplates;
	public static final String DOT_SEPARATOR = ".";
	public static final String TEMPLATE_EXTENSION = ".brt";
	public static final String TEMPLATE_MODEL_EXTENSION = ".brm";
	public static final String XML_EXTENSION= ".xml";
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
	
	public void build(File origen, File destino) throws IOException, FileNotFoundException
	{
		
		//System.out.println("Se encontro template " + fuente.getName() + " para modelo " + modelo.getName());
		
		
		
		for (Model modelo : this.modelos) 
		{
			Document doc = Jsoup.parse(origen, "UTF-8");
			doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
			Elements comodines = doc.getAllElements();
			
			String path = destino.getPath().replace("xxx", modelo.getName().toLowerCase());
			
			for (Element comodin : comodines) 
			{
				comodin = setNodeValue(modelo, comodin);
				  System.out.println("link.nodeName(): " + comodin.nodeName());
			}
			
			System.out.println("Data nodo link.text(): " + doc.getElementsByTag("body").text() + "\n" +
					  "link.html(): " + doc.getElementsByTag("body").html() + "\n" +
					  "link.outerHtml(): " + doc.getElementsByTag("body").outerHtml() + "\n" +
					  "link.toString(): " + doc.getElementsByTag("body").toString() + "\n" +
					  "link.val(): " + doc.getElementsByTag("body").val() + "\n\n" );
			//"link.className(): " + links.className() + "\n" +
			//"link.cssSelector(): " + links.cssSelector() + "\n" +
			//"link.data(): " + links.data() + "\n" +
			//"link.ownText(): " + links.ownText() + "\n" +
			//"link.tagName(): " + links.tagName() + "\n" +
			//"link.id(): " + links.id() + "\n" +
			//"link.nodeName(): " + links.nodeName() + "\n" +
		
			String texto = doc.getElementsByTag("body").html();
			
			File btm = new File(path);
			BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(btm), "UTF-8"));
			fileWriter.write(texto);
			fileWriter.flush();
			fileWriter.close();
			
			System.out.println("Archivo creado de " + origen + " a " + btm);
			
		}
		
	}
	
	
	public Element setNodeValue(Model modelo, Element nodo)
	{
		
		Inflector inf = new Inflector();
		TextNode text;
		TextNode textEmpty = new TextNode("", "");
		
		switch (nodo.nodeName()) 
		  {
			  case "modelname":
				  
				  text = new TextNode(modelo.getName(), "");
				  if(modelo == null || modelo.getName() == null || text == null || nodo == null)
				  {
					  System.out.println("Data nodo nodo.text(): " + nodo.text() + "\n" +
							  "nodo.html(): " + nodo.html() + "\n" +
							  "nodo.outerHtml(): " + nodo.outerHtml() + "\n" +
							  "nodo.toString(): " + nodo.toString() + "\n" +
							  "nodo.val(): " + nodo.val() + "\n\n" );
				  }
				  nodo.replaceWith(text);
			  break;
			  case "modelnamelower":
				  text = new TextNode(inf.singularize(modelo.getName()).toLowerCase(), "");
				  nodo.replaceWith(text);
			  break;
			  case "modelnameplural":
				  text = new TextNode(inf.pluralize(modelo.getName()), "");
				  nodo.replaceWith(text);
			  break;
			  case "properties":
				  
				  Elements propiedades = new Elements();
				  
				  for (com.brgenerator.entities.Model.Properties.Property propiedad : modelo.getProperties().getProperty()) 
				  {
					  
					  Element nodoCopia = nodo.clone();
					  
						for (int i = 0; i < nodoCopia.getAllElements().size(); i++) 
						{
							
							System.out.println("nodoCopia.getAllElements().get(i).nodeName(): " + nodoCopia.getAllElements().get(i).nodeName() );
							
							switch (nodoCopia.getAllElements().get(i).nodeName()) 
							{
								case "name":
									if(propiedad.getName() != null)
									{
										if(nodoCopia.getAllElements().get(i).childNodes().isEmpty())
										{
											text = new TextNode(propiedad.getName(), "");
											nodoCopia.getAllElements().get(i).replaceWith(text);
											i--;
										}
										else
										{
											text = new TextNode(propiedad.getName(), "");
											nodoCopia.getAllElements().get(i).getElementsByTag("value").get(0).replaceWith(text);
	//										System.out.println("nodoCopia.getAllElements().get(i).text(): " + nodoCopia.getAllElements().get(i).text() + "\n\n");
											String textAux = nodoCopia.getAllElements().get(i).text();
	//										System.out.println("textoNuevo: " + textoNuevo + "\n\n" );
											TextNode textNodeAux = new TextNode(textAux, "");
											nodoCopia.getAllElements().get(i).replaceWith(textNodeAux);
											i--;
										}
									}
									else
									{
										nodoCopia.getAllElements().get(i).remove();
										i--;
									}
									
								break;
								case "default":
									if(propiedad.getDefault() != null)
									{
										if(nodoCopia.getAllElements().get(i).childNodes().isEmpty())
										{
											text = new TextNode(propiedad.getDefault().getValue(), "");
											nodoCopia.getAllElements().get(i).replaceWith(text);
											i--;
										}
										else
										{
											text = new TextNode(propiedad.getDefault().getValue(), "");
											nodoCopia.getAllElements().get(i).getElementsByTag("value").get(0).replaceWith(text);
	//										System.out.println("nodoCopia.getAllElements().get(i).text(): " + nodoCopia.getAllElements().get(i).text() + "\n\n");
											String textAux = nodoCopia.getAllElements().get(i).text();
	//										System.out.println("textoNuevo: " + textoNuevo + "\n\n" );
											TextNode textNodeAux = new TextNode(textAux, "");
											nodoCopia.getAllElements().get(i).replaceWith(textNodeAux);
											i--;
										}
									}
									else
									{
										nodoCopia.getAllElements().get(i).remove();
										i--;
									}
								break;
								case "type":
									if(propiedad.getType() != null)
									{
										if(nodoCopia.getAllElements().get(i).childNodes().isEmpty())
										{
											text = new TextNode(propiedad.getType(), "");
											nodoCopia.getAllElements().get(i).replaceWith(text);
											i--;
										}
										else
										{
											text = new TextNode(propiedad.getType(), "");
											nodoCopia.getAllElements().get(i).getElementsByTag("value").get(0).replaceWith(text);
	//										System.out.println("nodoCopia.getAllElements().get(i).text(): " + nodoCopia.getAllElements().get(i).text() + "\n\n");
											String textAux = nodoCopia.getAllElements().get(i).text();
	//										System.out.println("textoNuevo: " + textoNuevo + "\n\n" );
											TextNode textNodeAux = new TextNode(textAux, "");
											nodoCopia.getAllElements().get(i).replaceWith(textNodeAux);
											i--;
										}
									}
									else
									{
										nodoCopia.getAllElements().get(i).remove();
										i--;
									}
								break;
								case "required":
									if(propiedad.getRequired() != null)
									{
										if(nodoCopia.getAllElements().get(i).childNodes().isEmpty())
										{
											text = new TextNode(propiedad.getRequired().getValue(), "");
											nodoCopia.getAllElements().get(i).replaceWith(text);
											i--;
										}
										else
										{
											text = new TextNode(propiedad.getRequired().getValue(), "");
											nodoCopia.getAllElements().get(i).getElementsByTag("value").get(0).replaceWith(text);
	//										System.out.println("nodoCopia.getAllElements().get(i).text(): " + nodoCopia.getAllElements().get(i).text() + "\n\n");
											String textAux = nodoCopia.getAllElements().get(i).text();
	//										System.out.println("textoNuevo: " + textoNuevo + "\n\n" );
											TextNode textNodeAux = new TextNode(textAux, "");
											nodoCopia.getAllElements().get(i).replaceWith(textNodeAux);
											i--;
										}
									}
									else
									{
										nodoCopia.getAllElements().get(i).remove();
										i--;
									}
								break;
								case "ref":
//									if(propiedad.getRelation() != null)
//									{
//										if(nodoCopia.getAllElements().get(i).childNodes().isEmpty())
//										{
//											System.out.println("Test1");
//										}
//										else
//										{
//											System.out.println("Test2");
//										}
//									}
//									else
//									{
//										nodoCopia.getAllElements().get(i).remove();
//										i--;
//									}
								break;
								case "trim":
									if(propiedad.getTrim() != null)
									{
										if(nodoCopia.getAllElements().get(i).childNodes().isEmpty())
										{
											text = new TextNode(propiedad.getTrim().getValue().toString(), "");
											nodoCopia.getAllElements().get(i).replaceWith(text);
											i--;
										}
										else
										{
											text = new TextNode(propiedad.getTrim().getValue().toString(), "");
											nodoCopia.getAllElements().get(i).getElementsByTag("value").get(0).replaceWith(text);
	//										System.out.println("nodoCopia.getAllElements().get(i).text(): " + nodoCopia.getAllElements().get(i).text() + "\n\n");
											String textAux = nodoCopia.getAllElements().get(i).text();
	//										System.out.println("textoNuevo: " + textoNuevo + "\n\n" );
											TextNode textNodeAux = new TextNode(textAux, "");
											nodoCopia.getAllElements().get(i).replaceWith(textNodeAux);
											i--;
										}
									}
									else
									{
										nodoCopia.getAllElements().get(i).remove();
										i--;
									}
								break;
								default:
								break;
								
							}
						}
						
//						System.out.println("nodoCopia.getAllElements().html(): " + nodoCopia.getAllElements().html() +"\n\n");
//						System.out.println("nodoCopia.html(): " + nodoCopia.html() +"\n\n");
						
						propiedades.add(nodoCopia);
					  
				  	}
				  	
				  for (Element element : propiedades) 
				  {
					    nodo.parent().appendChild(element);
				  }
				  
				  nodo.remove();
					
				  break;
				  default:
				  {
					}
					  
				  break;
			  }

		System.out.println("Data nodo nodo.text(): " + nodo.text() + "\n" +
				  "nodo.html(): " + nodo.html() + "\n" +
				  "nodo.outerHtml(): " + nodo.outerHtml() + "\n" +
				  "nodo.toString(): " + nodo.toString() + "\n" +
				  "nodo.val(): " + nodo.val() + "\n\n" );
		
		return nodo;
	}

	

}