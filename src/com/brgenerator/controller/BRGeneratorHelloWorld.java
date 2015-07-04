package com.brgenerator.controller;

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
		
		GeneratorManager gm = new GeneratorManager(servletContext);
		gm.run();
		
		String message = "<br><div style='text-align:center;'>"
				+ "<h3>********** Hello World, Spring MVC Tutorial</h3>This message is coming from CrunchifyHelloWorld.java *****pepe*****</div><br><br>";
		return new ModelAndView("welcome", "message", message);
	}
}

