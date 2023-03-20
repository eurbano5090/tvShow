package com.bolsadeideas.springboot.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

	@Value("${spring-boot-data-jpa.ruta.imagenes}")
	private String rutaImagenes;
	
	@Value("${spring-boot-data-jpa.ruta.actores}")
	private String rutaActores;
	
	public void addResourceHandlers(ResourceHandlerRegistry registry) { 
		registry.addResourceHandler("/uploads/**").addResourceLocations("file:"+rutaImagenes);
		registry.addResourceHandler("/img-actores/**").addResourceLocations("file:"+rutaActores);
	;
	}

}
