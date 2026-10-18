package com.sistemas.controlador;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	@GetMapping("/login")
	public String login(Model model) {
		return "usuario/paginaLogin";
	}
	
	@GetMapping("/denegado")
	public String accesoDenegado(Authentication authResult, Model model) {
		String role = authResult == null ? "" : authResult.getAuthorities().toString();
		model.addAttribute("roles", role);
		
		return "usuario/pagina403";
	}

}
