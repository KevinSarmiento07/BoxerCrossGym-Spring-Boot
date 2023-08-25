package co.com.boxercrossgym.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/entrenamiento")
public class EntrenamientoController {

	
	@GetMapping("/calendario")
	public String calendario() {
		
		
		return "admin/calendario";
	}
	
	@GetMapping("/agregar")
	public String nuevoEntreno(Model model) {
		return "admin/agregarEntrenamiento";
	}
}
