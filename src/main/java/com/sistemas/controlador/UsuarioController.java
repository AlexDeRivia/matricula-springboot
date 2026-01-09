package com.sistemas.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sistemas.entidad.Usuario;
import com.sistemas.servicio.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	@Autowired private UsuarioService usuarioService;
	
	@GetMapping("/poblar")
	public String poblar() {
		//solo para datos de prueba no es requisito funcional
		usuarioService.agregar(new Usuario("Juan", "123456", "ADMIN", 1));
		usuarioService.agregar(new Usuario("Carlos", "123456", "USUARIO", 1));
		usuarioService.agregar(new Usuario("Pedro", "123456", "USUARIO", 1));
		
		return "index";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		return "usuario/paginaLogin";
	}
	
	@GetMapping("/denegado")
	public String accesoDenegado(Authentication authResult, Model model) {
		String role = authResult.getAuthorities().toString();
		model.addAttribute("roles", role);
		
		return "Usuario/pagina403";
	}

}
