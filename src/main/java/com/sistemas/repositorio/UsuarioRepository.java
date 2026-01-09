package com.sistemas.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistemas.entidad.Usuario;

public interface UsuarioRepository extends JpaRepository <Usuario, Long>{

}
