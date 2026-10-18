package com.sistemas.controlador;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import com.sistemas.entidad.Alumno;
import com.sistemas.entidad.Curso;
import com.sistemas.entidad.Nota;
import com.sistemas.entidad.NotaID;
import com.sistemas.servicio.AlumnoService;
import com.sistemas.servicio.CursoService;
import com.sistemas.servicio.NotaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/nota")
public class NotaController {
	
	@Autowired private NotaService notaService;
	@Autowired private AlumnoService alumnoService;
	@Autowired private CursoService cursoService;
	
	@GetMapping("/matricular/{idAlumno}")
	public String getMatriculaForm(Model model, @PathVariable Long idAlumno) {
		
		Alumno alumno = alumnoService.buscar(idAlumno);
		if (alumno == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		List<Long> idCursosActuales = notaService.listarPorAlumno(idAlumno).stream().map(nota -> nota.getCurso().getId()).collect(Collectors.toList());
		
		model.addAttribute("alumno", alumno);
		model.addAttribute("listaCursos", cursoService.listarTodos());
		model.addAttribute("idCursosActuales", idCursosActuales);
		
		return "nota/notaForm";
	}

	@GetMapping("/boleta/{idAlumno}")
	public String boleta(@PathVariable Long idAlumno, Model model) {
		Alumno alumno = alumnoService.buscar(idAlumno);
		if (alumno == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		model.addAttribute("alumno", alumno);
		model.addAttribute("notas", notaService.listarPorAlumno(idAlumno));
		return "nota/boleta";
	}

	@GetMapping("/calificar/{idAlumno}/{idCurso}")
	public String editarCalificaciones(@PathVariable Long idAlumno, @PathVariable Long idCurso, Model model) {
		Nota nota = buscarMatricula(idAlumno, idCurso);
		CalificacionForm form = new CalificacionForm();
		form.setUnidad1(nota.getUnidad1());
		form.setUnidad2(nota.getUnidad2());
		form.setUnidad3(nota.getUnidad3());
		prepararFormulario(model, nota, form);
		return "nota/calificacionForm";
	}

	@PostMapping("/calificar/{idAlumno}/{idCurso}")
	public String guardarCalificaciones(@PathVariable Long idAlumno, @PathVariable Long idCurso,
			@Valid @ModelAttribute("calificacion") CalificacionForm form, BindingResult errors, Model model,
			RedirectAttributes redirectAttributes) {
		Nota nota = buscarMatricula(idAlumno, idCurso);
		if (errors.hasErrors()) {
			prepararFormulario(model, nota, form);
			return "nota/calificacionForm";
		}
		nota.setUnidad1(form.getUnidad1());
		nota.setUnidad2(form.getUnidad2());
		nota.setUnidad3(form.getUnidad3());
		notaService.actualizar(nota);
		redirectAttributes.addFlashAttribute("success", "Calificaciones guardadas.");
		return "redirect:/nota/boleta/" + idAlumno;
	}

	private Nota buscarMatricula(Long idAlumno, Long idCurso) {
		Nota nota = notaService.buscar(new NotaID(idAlumno, idCurso));
		if (nota == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		return nota;
	}

	private void prepararFormulario(Model model, Nota nota, CalificacionForm form) {
		model.addAttribute("alumno", nota.getAlumno());
		model.addAttribute("curso", nota.getCurso());
		model.addAttribute("calificacion", form);
	}
	
	@PostMapping("/matricula/guardar")
	@Transactional
	public String postMatriculaForm(@RequestParam Long id,
			@RequestParam(required = false) List<Long> idSeleccionados,
			RedirectAttributes redirectAttributes) {
		if (alumnoService.buscar(id) == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		List<Long> seleccionados = idSeleccionados == null ? List.of() : idSeleccionados.stream().distinct().toList();
		for (Long idCurso : seleccionados) {
			if (cursoService.buscar(idCurso) == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		List<Nota> notasActuales = notaService.listarPorAlumno(id);
		List<Nota> desmarcadas = notasActuales.stream()
				.filter(nota -> !seleccionados.contains(nota.getCurso().getId())).toList();
		if (desmarcadas.stream().anyMatch(Nota::tieneCalificaciones)) {
			redirectAttributes.addFlashAttribute("error", "No se puede retirar una matrícula con calificaciones. Conserve ese curso o borre sus notas antes de retirarlo.");
			return "redirect:/nota/matricular/" + id;
		}
		desmarcadas.forEach(nota -> notaService.eliminar(new NotaID(id, nota.getCurso().getId())));
		List<Long> idsCursosActuales = notasActuales.stream()
				.map(nota -> nota.getCurso().getId()).collect(Collectors.toList());
		
		//Añadir solo los nuevos seleccionados
		if(!seleccionados.isEmpty())
			seleccionados.stream().filter(idCurso -> !idsCursosActuales.contains(idCurso)).forEach(idCurso ->{
				Nota notaAgregar = new Nota();
				notaAgregar.setAlumno(alumnoService.buscar(id));
				notaAgregar.setCurso(cursoService.buscar(idCurso));
				notaService.agregar(notaAgregar);
			});
		redirectAttributes.addFlashAttribute("success", "Matrícula actualizada.");
		return "redirect:/alumno/index";
		
		
	}
	
	

}
