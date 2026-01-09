package com.sistemas.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sistemas.entidad.Alumno;
import com.sistemas.servicio.AlumnoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(path = "/alumno")
public class AlumnoController {

	@Autowired private AlumnoService alumnoService;
	
	@GetMapping({"/","/index"})
	public String getIndex(Model model) {
		model.addAttribute("listaAlumnos", alumnoService.listarTodos());
		return "alumno/alumnoIndex";
	}
	
	@GetMapping("/nuevo")
	public String MostrarFormularioNuevo(Model model) {
		model.addAttribute("alumno", new Alumno());
		return "alumno/alumnoForm";
	}
	
	@GetMapping("/editar/{id}")
	public String MostrarFormularioEditar(Model model, @PathVariable("id") Long idAlumno) {
		
		Alumno aux = alumnoService.buscar(idAlumno);
		model.addAttribute("alumno", aux!=null ? aux:new Alumno());
		return "alumno/alumnoForm";
	}
	
	@GetMapping("/guardar")
	public String ProcesarFormulario(Model model, @Valid @ModelAttribute("alumno") Alumno alumno, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) 
			return "alumno/alumnoForm";
		if(alumno.getId() == null)
			alumnoService.agregar(alumno);
		else 
			alumnoService.actualizar(alumno);
		
		return "redirect:/alumno/index";
	}
	
	@GetMapping("/eliminar/{id}")
	public String alumnoEliminr(Model model, @PathVariable("id") Long idAlumno) {
		alumnoService.eliminar(idAlumno);
		return "redirect:/alumno/index";
	}
}