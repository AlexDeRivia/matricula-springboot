package com.sistemas.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sistemas.entidad.Docente;
import com.sistemas.servicio.DocenteService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/docente")
public class DocenteController {
	
	@Autowired private DocenteService docenteService;
	
	//Atiende peticiones GET de la ruta "/docente/index" o "docente"
	@GetMapping({"/index", ""})
	public String indice(Model model) {
		model.addAttribute("listaDocentes", docenteService.listarTodos());
		//devuelve la plantila "docenteIndex.html" de la carpeta "docente"
		return "docente/docenteIndex";
	}
	
	//Atiende peticiones GET de la ruta "/docente/nuevo"
	@GetMapping("/nuevo")
	public String docenteNuevoForm(Model model) {
		model.addAttribute("docente", new Docente());
		//devuelve la plantilla "docenteForm.html" de la carpeta "docente"
		return "docente/docenteForm";
	}
	
	//Atiende peticiones POST de la ruta "/docente/guardar"
	@PostMapping("/guardar")
	public String docenteNuevoProcesar(
			@Valid @ModelAttribute("docente") Docente docente,
			BindingResult bindingResult,
			Model model) {
		if(bindingResult.hasErrors())
			//si hubo errores mestra el formulario para corregir
			return "docente/docenteForm";
		if(docente.getId()==null)
			docenteService.agregar(docente);
		else
			docenteService.actualizar(docente);
		return "redirect;/docente/index";
	}
	
	//Atiende peticiones GET de la ruta "/docente/editar/{id}"
	@GetMapping("/editar/{id}")
	public String docenteEditarFormModel(Model model,
			@PathVariable("id") Long id) {
		
		Docente buscado = docenteService.buscar(id);
		model.addAttribute("docente", buscado!=null ? buscado : new Docente());
		
		return "dicente/docenteForm";
	}
	
	//Atiende peticiones GET de la ruta "/docente/eliminar/{id}"
	@GetMapping("/eliminar/{id}")
	public String docenteEliminar(Model model,
			@PathVariable("id") Long id) {
		
		docenteService.eliminar(id);
		return "redirect;/docente/index";
	}

}
