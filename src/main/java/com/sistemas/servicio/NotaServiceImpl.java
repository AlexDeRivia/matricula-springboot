package com.sistemas.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistemas.entidad.Nota;
import com.sistemas.entidad.NotaID;
import com.sistemas.repositorio.NotaRepository;

@Service
public class NotaServiceImpl implements NotaService{

	@Autowired
	private NotaRepository notaRepository;
	
	@Override
	public Nota agregar (Nota entidad) {
		return notaRepository.save(entidad);
	}
	
	@Override
	public List<Nota> listarTodos(){
		return notaRepository.findAll();
	}
	
	@Override
	public Nota buscar (NotaID id) {
		return notaRepository.findById(id).orElse(null);
	}
	
	@Override
	public Nota actualizar(Nota entidad) {
		return notaRepository.save(entidad);
	}
	
	@Override
	public void eliminar (NotaID id) {
		notaRepository.deleteById(id);
	}
	
	public List<Nota> listarPorAlumno(Long idAlumno){
		return notaRepository.findByAlumnoId(idAlumno);
	}
}
