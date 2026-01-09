package com.sistemas.servicio;

import java.util.List;
import com.sistemas.entidad.Nota;
import com.sistemas.entidad.NotaID;

public interface NotaService extends iGenericoService<Nota, NotaID>{
	
	public List<Nota> listarPorAlumno(Long idAlumno);

}
