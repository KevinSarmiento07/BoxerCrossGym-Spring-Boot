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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.util.JSONPObject;

import co.com.boxercrossgym.entity.Bloque;
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


	/* Esto es para mandar el nombre del usuario autenticado y se pueda visualizar en el menu  */
	@ModelAttribute("nombre")
		public String nombre() {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String nombre = "";
			try{
				nombre = userDetailsService.findByUsername(auth.getName()).getNombre() + " " + userDetailsService.findByUsername(auth.getName()).getApellido();
			}catch(Exception e){
				e.getMessage();
			}
			return nombre != null ?  nombre : "";
		
	}


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


	@PostMapping(path = ("/agregar"))
	public String procesarNuevoEntreno(@Valid Entrenamiento entrenamiento,BindingResult result, Model model, @RequestParam(name =  "descripciones[]", required = false) String descripciones[],
	 @RequestParam(name = "nombres[]", required = false) String nombres[], RedirectAttributes flash){
		System.out.println("entrando al metodo post agregar");

		

	
		if(result.hasErrors() || nombres == null || descripciones == null){
			model.addAttribute("titulo", "Nuevo Entrenamiento");
			model.addAttribute("error", "Hubo un error al guardar el entrenamiento.");
			return "admin/agregarEntrenamiento";
		}

		for(int i = 0; i < nombres.length; i++){

			Bloque bloque = new Bloque();
			bloque.setNombre(nombres[i]);
			bloque.setDescripcion(descripciones[i]);

			entrenamiento.addBloque(bloque);

		}

		entrenamientoService.save(entrenamiento);

		flash.addFlashAttribute("success", "El entreno fue creado exitosamente");

		System.out.println(entrenamiento.toString());
		return "redirect:/user/home";
	}
}
