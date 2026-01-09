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



@Controller
@RequestMapping(path = "/curso")
public class CursoController {
@Autowired private CursoService cursoService;
@Autowired private DocenteService docenteService;

	@GetMapping({"/","index"})
public String index(Model model) {
	model.addAttribute("listaCursos",cursoService.listarTodos());
	return "curso/cursoIndex.html";
	}

  //Atiende la peticion de GET de la ruta "/curso/nuevo"
@GetMapping("/nuevo")
public String mostrarFormularioNuevo(Model model) {
 model.addAttribute("curso", new Curso());
 model.addAttribute("listaDocentes", docenteService.listarTodos());
 return "curso/cursoForm.html";
 
}
  
//Atiende la peticion de POST de la ruta "/curso/guardar"
@PostMapping ("/guardar")
public String procesarFormulario (Model model,
		@Valid @ModelAttribute("curso") Curso curso,
		BindingResult bindingResult) {
	
	if(bindingResult.hasErrors()) {
		model.addAttribute("listaDocentes",docenteService.listarTodos());
		return "curso/cursoForm.html";
		
	}
	
	if(curso.getId() == null)
		cursoService.agregar(curso);
	else 
		cursoService.actualizar(curso);
	return "redirect: /curso/index";
}
 
//Atiende peticiones GET de la ruta "/curso/editar/{id}"

@GetMapping("/editar/{id}")
public String mostrarFormularioNuevo(Model model,
		@PathVariable("id") Long id) {
	
	Curso buscado = cursoService.buscar(id);
 model.addAttribute("curso", buscado!=null ? buscado : new Curso());
 model.addAttribute("listaDocentes", docenteService.listarTodos());
 return "curso/cursoForm";

}
	//Atiente peticiones GET de la ruta "/curso/eliminar/{id}"
@GetMapping("/eliminar/{id}")
public String cursoEliminar(Model model,
		@PathVariable("id") Long id) {
	
	cursoService.eliminar(id);
	return "redirect:/curso/index";
}
}
