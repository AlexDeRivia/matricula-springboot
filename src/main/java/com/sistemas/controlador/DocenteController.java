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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sistemas.entidad.Docente;
import com.sistemas.servicio.DocenteService;
import com.sistemas.servicio.RegistroEnUsoException;

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
			Model model, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors())
			//si hubo errores mestra el formulario para corregir
			return "docente/docenteForm";
		boolean nuevo = docente.getId() == null;
		if(nuevo)
			docenteService.agregar(docente);
		else
			docenteService.actualizar(docente);
		redirectAttributes.addFlashAttribute("success", nuevo ? "Docente registrado." : "Docente actualizado.");
		return "redirect:/docente/index";
	}
	
	//Atiende peticiones GET de la ruta "/docente/editar/{id}"
	@GetMapping("/editar/{id}")
	public String docenteEditarFormModel(Model model,
			@PathVariable("id") Long id) {
		
		Docente buscado = docenteService.buscar(id);
		if (buscado == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		model.addAttribute("docente", buscado);
		
		return "docente/docenteForm";
	}
	
	//Atiende peticiones GET de la ruta "/docente/eliminar/{id}"
	@PostMapping("/eliminar/{id}")
	public String docenteEliminar(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		try {
			docenteService.eliminar(id);
			redirectAttributes.addFlashAttribute("success", "Docente eliminado.");
		} catch (RegistroEnUsoException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/docente/index";
	}

}
