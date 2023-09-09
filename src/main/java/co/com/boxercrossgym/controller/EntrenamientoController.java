package co.com.boxercrossgym.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import co.com.boxercrossgym.entity.Entrenamiento;
import co.com.boxercrossgym.service.IEntrenamientoService;
import co.com.boxercrossgym.service.JpaUserDetailsService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/entrenamiento")
public class EntrenamientoController {
	
	
	@Autowired
	@Qualifier("entrenaminetoServiceImple")
	private IEntrenamientoService entrenamientoService;
	
	@Autowired
	private JpaUserDetailsService userDetailsService;

//	@ModelAttribute("nombre")
//	public String nombre() {
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		return userDetailsService.findByUsername(auth.getName()).getNombre() + " " + userDetailsService.findByUsername(auth.getName()).getApellido();
//	}


	@InitBinder
	public void initBinder(WebDataBinder binder){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dddd");

		binder.registerCustomEditor(Date.class, "fechaEntreno", new CustomDateEditor(dateFormat, false));

	}

	
	@GetMapping("/calendario")
	public String calendario() {
		
		
		return "admin/calendario";
	}
	
	@GetMapping("/agregar")
	public String nuevoEntreno(Model model, Entrenamiento entrenamiento) {
		model.addAttribute("titulo", "Nuevo Entrenamiento");
		return "admin/agregarEntrenamiento";
	}


	@PostMapping("/agregar")
	public String procesarNuevoEntreno(@Valid Entrenamiento entrenamiento){

		System.out.println(entrenamiento.toString());

		return "redirect:/login";
	}
}
