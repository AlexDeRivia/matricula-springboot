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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.sistemas.entidad.Alumno;
import com.sistemas.servicio.AlumnoService;
import com.sistemas.servicio.RegistroEnUsoException;

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
		if (aux == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		model.addAttribute("alumno", aux);
		return "alumno/alumnoForm";
	}
	
	@PostMapping("/guardar")
	public String ProcesarFormulario(Model model, @Valid @ModelAttribute("alumno") Alumno alumno, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		
		if(bindingResult.hasErrors()) 
			return "alumno/alumnoForm";
		boolean nuevo = alumno.getId() == null;
		if(nuevo)
			alumnoService.agregar(alumno);
		else 
			alumnoService.actualizar(alumno);
		redirectAttributes.addFlashAttribute("success", nuevo ? "Alumno registrado." : "Datos del alumno actualizados.");
		return "redirect:/alumno/index";
	}
	
	@PostMapping("/eliminar/{id}")
	public String alumnoEliminr(@PathVariable("id") Long idAlumno, RedirectAttributes redirectAttributes) {
		try {
			alumnoService.eliminar(idAlumno);
			redirectAttributes.addFlashAttribute("success", "Alumno eliminado.");
		} catch (RegistroEnUsoException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/alumno/index";
	}
}
