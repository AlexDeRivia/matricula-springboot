package com.sistemas.controlador;

import org.springframework.validation. BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation. ModelAttribute;
import  org.springframework.web.bind.annotation. PathVariable;
import  org.springframework.web.bind.annotation. PostMapping; 
import org.springframework.web.bind.annotation. RequestMapping;

import com.sistemas.entidad. Curso;
import com.sistemas.servicio.CursoService;
import com.sistemas.servicio.DocenteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework. stereotype.Controller; 
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.sistemas.servicio.RegistroEnUsoException;



@Controller
@RequestMapping(path = "/curso")
public class CursoController {
@Autowired private CursoService cursoService;
@Autowired private DocenteService docenteService;

	@GetMapping({"/","index"})
public String index(Model model) {
	model.addAttribute("listaCursos",cursoService.listarTodos());
	return "curso/cursoIndex";
	}

  //Atiende la peticion de GET de la ruta "/curso/nuevo"
@GetMapping("/nuevo")
public String mostrarFormularioNuevo(Model model) {
 model.addAttribute("curso", new Curso());
 model.addAttribute("listaDocentes", docenteService.listarTodos());
 return "curso/cursoForm";
 
}
  
//Atiende la peticion de POST de la ruta "/curso/guardar"
@PostMapping ("/guardar")
public String procesarFormulario (Model model,
		@Valid @ModelAttribute("curso") Curso curso,
		BindingResult bindingResult, RedirectAttributes redirectAttributes) {
	if (curso.getDocente() == null || curso.getDocente().getId() == null
			|| docenteService.buscar(curso.getDocente().getId()) == null) {
		bindingResult.rejectValue("docente", "docente.invalido", "Seleccione un docente válido");
	}
	
	if(bindingResult.hasErrors()) {
		model.addAttribute("listaDocentes",docenteService.listarTodos());
		return "curso/cursoForm";
		
	}
	
	boolean nuevo = curso.getId() == null;
	if(nuevo)
		cursoService.agregar(curso);
	else 
		cursoService.actualizar(curso);
	redirectAttributes.addFlashAttribute("success", nuevo ? "Curso registrado." : "Curso actualizado.");
	return "redirect:/curso/index";
}
 
//Atiende peticiones GET de la ruta "/curso/editar/{id}"

@GetMapping("/editar/{id}")
public String mostrarFormularioNuevo(Model model,
		@PathVariable("id") Long id) {
	
	Curso buscado = cursoService.buscar(id);
 if (buscado == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
 model.addAttribute("curso", buscado);
 model.addAttribute("listaDocentes", docenteService.listarTodos());
 return "curso/cursoForm";

}
	//Atiente peticiones GET de la ruta "/curso/eliminar/{id}"
@PostMapping("/eliminar/{id}")
public String cursoEliminar(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
	try {
		cursoService.eliminar(id);
		redirectAttributes.addFlashAttribute("success", "Curso eliminado.");
	} catch (RegistroEnUsoException ex) {
		redirectAttributes.addFlashAttribute("error", ex.getMessage());
	}
	return "redirect:/curso/index";
}
}
