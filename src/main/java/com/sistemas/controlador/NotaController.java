package com.sistemas.controlador;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sistemas.entidad.Alumno;
import com.sistemas.entidad.Curso;
import com.sistemas.entidad.Nota;
import com.sistemas.entidad.NotaID;
import com.sistemas.servicio.AlumnoService;
import com.sistemas.servicio.CursoService;
import com.sistemas.servicio.NotaService;

@Controller
@RequestMapping("/nota")
public class NotaController {
	
	@Autowired private NotaService notaService;
	@Autowired private AlumnoService alumnoService;
	@Autowired private CursoService cursoService;
	
	@GetMapping("/matricular/{idAlumno}")
	public String getMatriculaForm(Model model, @PathVariable Long idAlumno) {
		
		Alumno alumno = alumnoService.buscar(idAlumno);
		List<Long> idCursosActuales = notaService.listarPorAlumno(idAlumno).stream().map(nota -> nota.getCurso().getId()).collect(Collectors.toList());
		
		model.addAttribute("alumno", alumno==null ? new Alumno():alumno);
		model.addAttribute("listarCursos", cursoService.listarTodos());
		model.addAttribute("idCursosActuales", idCursosActuales);
		
		return "nota/notaForm";
	}
	
	@PostMapping("/matricula/guardar")
	public String postMatriculaForm(@RequestParam Long id,
			@RequestParam(required = false) List<Long> idSeleccionados) {
		
		Stream<Nota> notasActuales1 = notaService.listarPorAlumno(id).stream();
		Stream<Nota> notasActuales2 = notaService.listarPorAlumno(id).stream();
		
		//Eliminar cursos desmarcados
		notasActuales1.filter(nota -> idSeleccionados == null ? true :!idSeleccionados.contains(nota.getCurso().getId())).forEach(
				nota -> notaService.eliminar(new NotaID(id, nota.getCurso().getId())));
		
		//lista de id de cursos actualmente matriculados
		List<Long> idsCursosActuales = notasActuales2.map(nota -> nota.getCurso().getId()).collect(Collectors.toList());
		
		//Añadir solo los nuevos seleccionados
		if(idSeleccionados != null)
			idSeleccionados.stream().filter(idCurso -> !idsCursosActuales.contains(idCurso)).forEach(idCurso ->{
				Nota notaAgregar = new Nota();
				Alumno alumno = new Alumno();
				Curso curso = new Curso();
				
				alumno.setId(id);
				curso.setId(idCurso);
				notaAgregar.setAlumno(alumno);
				notaAgregar.setCurso(curso);
				notaService.agregar(notaAgregar);
			});
		
		return "redirect:/alumno/index";
		
		
	}
	
	

}
