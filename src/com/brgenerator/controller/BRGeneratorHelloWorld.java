package com.brgenerator.controller;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.brgenerator.*;
 
/*
 * author: Crunchify.com
 * 
 */
 
@Controller
public class BRGeneratorHelloWorld {
	
	@Autowired
	ServletContext servletContext;
 
	@RequestMapping("/welcome")
	public ModelAndView helloWorld() 
	{
		File origen = new File(servletContext.getRealPath("/WEB-INF/resources/Base/"));
    	File destino = new File(servletContext.getRealPath("/WEB-INF/resources/Destino/"));
    	File modelos = new File( servletContext.getRealPath("/WEB-INF/resources/Model/"));
		GeneratorManager gm = new GeneratorManager(origen, destino, modelos);
		gm.run();
		
		String message = "<br><div style='text-align:center;'>"
				+ "<h3><div>Templates generados con exito!</div></h3><br>"
				+ "<div><b>Origen de los recursos:</b> " + origen.getAbsolutePath() + "</div>"
				+ "<div><b>Destino de los recursos:</b> " + destino.getAbsolutePath() + "</div>"
				+ "<div><b>Modelos:</b> " + modelos.getAbsolutePath() + "</div>";
		return new ModelAndView("welcome", "message", message);
	}
}

