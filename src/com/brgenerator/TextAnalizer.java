package com.brgenerator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.brgenerator.entities.TemplateObject;
import com.brgenerator.entities.TemplateObjectAtt;
import com.brgenerator.entities.TemplateObject.Type;

public class TextAnalizer 
{
	
	public TemplateObject content;
	
	public TextAnalizer(File origen) throws IOException
	{
		Path path = Paths.get(origen.getAbsolutePath());
		Charset charset = StandardCharsets.UTF_8;
		this.content = new TemplateObject(new String(Files.readAllBytes(path), charset), 0,0, TemplateObject.Type.TEXT);
	}
	
	private String pTAG_MODELS = "(\\[model\\S*?])";
	
	
	private String pATTRIBUTES = "(\\S+)=\"(.*?)\"";
	private String pTAG_PROP_ELEMENTS = "\\[property:\\S*?](.*?)\\[/property:\\S*?]";
	//private String pTAG_ELEMENTS = "\\[property:.+/property:\\S*?]";
	private String pTAG_DIST_ELEM = "(\\[property:\\S[a-z]*?\\]|\\[property:\\S[a-z]*?\\:\\S[a-z]*?\\])(?![\\s\\S]*\\1)";
	
	
	private Pattern TAG_PROP_ELEMENTS = Pattern.compile(pTAG_PROP_ELEMENTS);
	//private Pattern TAG_ELEMENTS = Pattern.compile(pTAG_ELEMENTS);
	private Pattern TAG_MODELS = Pattern.compile(pTAG_MODELS);
	private Pattern TAG_DIST_ELEM = Pattern.compile(pTAG_DIST_ELEM);
	
	private String pELEMENT_PROPERTIES = "\\[properties.*?].+?\\[/properties]";
	private Pattern ELEMENT_PROPERTIES = Pattern.compile(pELEMENT_PROPERTIES,Pattern.DOTALL);
	
	
	// \[property:(\S[a-z]*?)\] valeeeeeeeeeee devuelve solo el nombre del att
	// \[property:\S[a-z]*?\] nodo att completo
	//  \[property:type].+\[/property:type] solo un nodo
    //	\[property:.+/property:\S*?] linea completa
	//  \[property:(.*?)/property:\S*?]
	//  \[property:\S*?](.*?)\[/property:\S*?]   FINAL
	//  (?s)\[properties](.*?)\[/properties] Todo lo contenido entre properties
	//  (?s)\[property:unique].+?\[/property:unique] todo lo contenido entre dos nodos
	//  \[property:(.*?)] == [property:type] == [property:type:value]
	// \[property:unique.*?]|\[/property:unique.*?]
	// \[property:\S[a-z]*?\:\S[a-z]*?\] todos los nodos triples
	// (?s)\[properties.*?].+?\[/properties] todo en properties
	// (?s)\[properties.*?] nodo properties
	// (\S+)=["]?((?:(?!["]?\s+(?:\S+)=|[>"]))+.)["]? llave valor atributos
	// (\S+)=["']?((?:.(?!["']?\s+(?:\S+)=|[>"']))+.)["']? Original
	// (\S+)="(.*?)" El que va
	
	/*Todo menos un espacio en blanco: + = ["] comillas dobles
	 * 
	 * ?:
	 * Coincide pero No captura 
	 * (?!
	 * 
	 * ["]?\s+(?:\S+)
	*/
	public TemplateObject findAndReplace(String wildcard, String replacement) throws IOException
	{
		this.content.setContent(this.content.getContent().replaceAll("\\" + wildcard, replacement));
		return content;
	}
	
	public static TemplateObject findAndReplace(TemplateObject content, String wildcard, String replacement) throws IOException
	{
		
		content.setContent(content.getContent().replace(wildcard, replacement));
		return content;
	}
	
	public static TemplateObject remove(TemplateObject content, String wildcard) throws IOException
	{
		
		String resultado = content.getContent();
	
		while (resultado.indexOf(wildcard) > -1)
		{
			int indiceInicio = resultado.indexOf(wildcard);
			int indiceFin = resultado.indexOf(wildcard) + wildcard.length();
			int tamañoTexto = resultado.length();
			String inicio = resultado.substring(0,indiceInicio);
			String fin = resultado.substring( indiceFin,tamañoTexto);
			resultado = (inicio.trim()+ fin);
		}
	    
		content.setContent(resultado);
		
	    return content;
	}
	
	public TemplateObject replace(TemplateObject element)
	{
		String resultado = this.content.getContent().substring(0, element.getFirstIndex()) + element.getContent() + content.getContent().substring(element.getLastIndex(), content.getContent().length()); 
		this.content.setContent(resultado);
		return content;
	}
	
	public static String findAndReplace(TemplateObject content, TemplateObject element)
	{
		String resultado = content.getContent().substring(0, element.getFirstIndex()) + element.getContent() + content.getContent().substring(element.getLastIndex(), content.getContent().length()); 
		content.setContent(resultado);
		return content.getContent();
	}
	
	public static String deleteTemplateObject(TemplateObject content, TemplateObject element)
	{
		String resultado = content.getContent().substring(0, element.getFirstIndex()) + content.getContent().substring(element.getLastIndex(), content.getContent().length()); 
		content.setContent(resultado);
		return content.getContent();
	}
	
	public List<String> getDistinctTagModels() 
	{
	    final List<String> tagValues = new ArrayList<String>();
	    final Matcher matcher = TAG_MODELS.matcher(this.content.getContent());
	    
	    while (matcher.find()) 
	    {
	    	if(!tagValues.contains(matcher.group(1)))
	        tagValues.add(matcher.group(1));
	    }
	    
	    return tagValues;
	}
	
	public List<String> getDistinctTagProperty() 
	{
	    final List<String> tagValues = new ArrayList<String>();
	    final Matcher matcher = TAG_DIST_ELEM.matcher(this.content.getContent());
	    
	    while (matcher.find()) 
	    {
	    	if(!tagValues.contains(matcher.group(1)))
	        tagValues.add(matcher.group(1));
	    }
	    
	    return tagValues;
	}
	
	
//	public static List<TemplateObject> getTagsProperty(TemplateObject content, String type) 
//	{
//	    String regx = "\\[property:"+type+"].+?\\[/property:"+type+"]";
//		Pattern patern = Pattern.compile(regx,Pattern.DOTALL);
//		
//	    final Matcher matcher = patern.matcher(content.getContent());
//	    List<TemplateObject> tos = new ArrayList<TemplateObject>();;
//	    
//	    while (matcher.find()) 
//	    {
//	        TemplateObject to = new TemplateObject(matcher.group(),matcher.start(0),matcher.end(0));
//	        tos.add(to);
//	    }
//	    
//	    return tos;
//	    
//	}
	
//	public static List<TemplateObject> getAllTagsPropertyByType(TemplateObject content, String type) 
//	{
//	    String regx = "\\[property:"+type+".*?]|\\[/property:"+type+".*?]";
//		Pattern patern = Pattern.compile(regx);
//		
//	    final Matcher matcher = patern.matcher(content.getContent());
//	    List<TemplateObject> tos = new ArrayList<TemplateObject>();;
//	    
//	    while (matcher.find()) 
//	    {
//	        TemplateObject to = new TemplateObject(matcher.group(),matcher.start(0),matcher.end(0));
//	        tos.add(to);
//	    }
//	    
//	    return tos;
//	    
//	}
	
//	public List<TemplateObject> getDistinctTagProperty(TemplateObject  properties) 
//	{
//	    
//		final Matcher matcher = TAG_DIST_ELEM.matcher(properties.getContent());
//	    List<TemplateObject> tos = new ArrayList<TemplateObject>();;
//	    
//	    while (matcher.find()) 
//	    {
//	        TemplateObject to = new TemplateObject(matcher.group(),0,0);
//	        if(!tos.contains(to))
//	        tos.add(to);
//	    }
//	    
//	    return tos;
//	}
	
	public List<String> getContentTagsProperty() 
	{
	    final List<String> tagValues = new ArrayList<String>();
	    final Matcher matcher = TAG_PROP_ELEMENTS.matcher(this.content.getContent());
	    while (matcher.find()) {
	        tagValues.add(matcher.group(1));
	    }
	    return tagValues;
	}
	
	public List<TemplateObject> getElementsProperties() 
	{
	    final Matcher matcher = ELEMENT_PROPERTIES.matcher(this.content.getContent());
	    List<TemplateObject> tos = new ArrayList<TemplateObject>();;
	    
	    while (matcher.find()) 
	    {
	        TemplateObject to = new TemplateObject(matcher.group(),matcher.start(0),matcher.end(0), TemplateObject.Type.PROPERTIES);
	        tos.add(to);
	    }
	    
	    return tos;
	}
	
//	public static List<TemplateObject> getContentTagsProperties(TemplateObject properties) 
//	{
//		String pCONTENT_PROPERTIES = "\\[properties](.*?)\\[/properties]";
//		Pattern CONTENT_PROPERTIES = Pattern.compile(pCONTENT_PROPERTIES,Pattern.DOTALL);
//		final Matcher matcher = CONTENT_PROPERTIES.matcher(properties.getContent());
//	    List<TemplateObject> tos = new ArrayList<TemplateObject>();;
//	    
//	    while (matcher.find()) 
//	    {
//	        TemplateObject to = new TemplateObject(matcher.group(1),matcher.start(1),matcher.end(1));
//	        tos.add(to);
//	    }
//	    
//	    return tos;
//	}
	
	public static TemplateObject setCuotes(TemplateObject content, int order) 
	{
		String aux = content.getContent().replaceAll("(\\[cuote:"+order+"])(?![\\s\\S]*\\1)", "");
		aux = aux.replaceAll("\\[cuote:"+order+"]", ",");
		content.setContent(aux);
	    return content;
	}
	
	
}
