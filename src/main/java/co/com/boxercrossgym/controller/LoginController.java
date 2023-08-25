package co.com.boxercrossgym.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.com.boxercrossgym.entity.Cliente;
import co.com.boxercrossgym.service.JpaUserDetailsService;
import jakarta.validation.Valid;

@Controller
public class LoginController {
	
	@Autowired
	JpaUserDetailsService usuarioService;

	
	
	@GetMapping("/login")
	public String login(@RequestParam( value = "error", required = false)String error , @RequestParam(value = "logout" , required = false)String logout , Model model, Principal principal, RedirectAttributes flash) {
		
		if(principal != null) {
			flash.addFlashAttribute("info", "Ya has iniciado sesion anteriormente");
			return "redirect:/";
		}
		
		if(logout != null) {
			model.addAttribute("success", "Has cerrado sesión con exito");
		}
		
		if(error != null) {
			System.out.println("entró en el if de ERROR");
			model.addAttribute("errorLogin", "Error al iniciar sesión, usuario o contraseña incorrecta");
			
		}
		
		model.addAttribute("titulo","BoxerCrossGym");
		
		return "login";
	}
	
	
	@GetMapping("/registrar")
	public String registrar(Cliente cliente, Model model) {
		return "registrar";
	}
	
	@PostMapping("/registrar")
	public String procesarRegistrar(@Valid Cliente cliente, BindingResult result, RedirectAttributes flash) {
		
		usuarioService.save(cliente);
		
		return "redirect:/listar";
	}
	
	@Autowired
	JpaUserDetailsService userDetailsService;
	
	@GetMapping("/error_403")
	public String error_403(Model model) {
		System.out.println("ENTRO EN /ERROR_403");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Cliente cliente = userDetailsService.findByUsername(auth.getName());
		
		if(cliente != null) {
			System.out.println("ENTRO EN if de /ERROR_403");
			model.addAttribute("cliente", cliente);
		}
		return "error/error_403";
	}
	
	

}
