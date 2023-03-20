package com.bolsadeideas.springboot.app.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Actor;
import com.bolsadeideas.springboot.app.models.entity.Categoria;
import com.bolsadeideas.springboot.app.models.service.CategoriaService;
import com.bolsadeideas.springboot.app.models.service.IActorService;
import com.bolsadeideas.springboot.app.util.Util;



@Controller
@RequestMapping("/actores")
public class ActoresController {
	
	@Autowired
	private IActorService actorService;
	
	@Autowired
	private CategoriaService icat;
	
	@GetMapping("/crear")
	public String crear(Actor actor) {
	
		return "actores/formActor";
	}
	
	
	@PostMapping("/save")
	public String guardar(Actor actor,BindingResult result,RedirectAttributes attributes,
			@RequestParam("archivoImagen") MultipartFile multiPart ) {
		if(result.hasErrors()) {
			for(ObjectError error:result.getAllErrors()) {
				System.out.println("Ocurrio un error :"+ error.getDefaultMessage());
			}
			return"actores/formActor";
		}
		if (!multiPart.isEmpty()) {
		
			String ruta = "c:/tvShow/img-actores/";
			String nombreImagen = Util.guardarArchivo(multiPart, ruta);
			if (nombreImagen != null){ 
			
			actor.setFoto(nombreImagen);
			}
		actorService.guardar(actor);
		attributes.addFlashAttribute("msg","Registro Guardado");
		System.out.println("Actor"+ actor);
	
		}return "redirect:/actores/indexPaginate";
		}
	
	@GetMapping("/edit/{id}")
    public String editar(@PathVariable("id") Long idActor,Model model) {
		Optional<Actor> actor = actorService.buscarPorId(idActor);
		model.addAttribute("actor",actor);
		return "actores/formActor";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	@GetMapping("/index")
	public String mostrarIndex(Model model) {
		List<Actor>lista=actorService.findAll();
		model.addAttribute("actores", lista);
		return "actores/listActores";
	}
	
	@GetMapping(value="/indexPaginate")
	public String mostrarIndexPaginado(Model model,Pageable page) {
		Page<Actor>lista=actorService.buscarTodas(page);
		model.addAttribute("actores",lista);
		return "actores/listActores";
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") Long idActor,RedirectAttributes attributes, Model model) {
		System.out.println("borrando Actor con id:" + idActor);
		actorService.eliminar(idActor);
		attributes.addFlashAttribute("msg","La vacante fue eliminada");
		return "redirect:/actores/index";
		
	}

	@GetMapping("/view/{id}")
	public String verDetalle(@PathVariable("id") Long idActor,Model model) {
		
		Optional<Actor> actor=actorService.buscarPorId(idActor);
		
		System.out.println("IdActor:" + idActor);
		model.addAttribute("actor",actor);
		
		return "detalle";
		
		
	}
}
