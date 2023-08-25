package co.com.boxercrossgym.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.com.boxercrossgym.dao.IClienteDao;
import co.com.boxercrossgym.entity.Cliente;
import co.com.boxercrossgym.paginator.PageRender;
import co.com.boxercrossgym.service.IClienteService;
import co.com.boxercrossgym.service.JpaUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@SessionAttributes("cliente")
@Controller
public class ClienteController {

	@Autowired
	JpaUserDetailsService userDetailsService;

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	@Qualifier(value = "clienteServiceImple")
	IClienteService clienteService;
	
	
	@ModelAttribute("nombre")
	public String nombre() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userDetailsService.findByUsername(auth.getName()).getNombre() + " " + userDetailsService.findByUsername(auth.getName()).getApellido();
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, "fechaNacimiento", new CustomDateEditor(dateFormat, false));
		binder.registerCustomEditor(Date.class, "fechaInscripcion", new CustomDateEditor(dateFormat, false));
	}
	
	
	public String clienteLogueado() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		return userDetailsService.findByUsername(auth.getName()).getNombre() + " " + userDetailsService.findByUsername(auth.getName()).getApellido();
	}

	@Secured(value = "ROLE_USER")
	@GetMapping(path = { "/", "/listar" })
	public String listar(@RequestParam(defaultValue = "0", name = "page") int page, Model model,
			@Param("termino") String termino, HttpServletRequest request) {
		System.out.println("TERMINO: " + termino);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Cliente cliente = userDetailsService.findByUsername(auth.getName());
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request,
				"ROLE_");

		if (securityContext.isUserInRole("ADMIN")) {
			logger.info("Hola ".concat(auth.getName())
					.concat(" tienes acceso desde SecurityContextHolderAwareRequestWrapper"));
		} else {
			logger.info("Hola ".concat(auth.getName())
					.concat(" no tienes acceso desde SecurityContextHolderAwareRequestWrapper"));
		}

		if (termino != null) {
			List<Cliente> clientes = clienteService.findAll(termino);
			model.addAttribute("clientes", clientes);
			model.addAttribute("titulo", "Listado De Clientes");
			return "admin/listarAdmin";
		}
		Sort sort = Sort.by(Sort.Direction.ASC, "nombre");
		Pageable pageRequest = PageRequest.of(page, 15, sort);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

		model.addAttribute("clientes", clientes);
		model.addAttribute("titulo", "Listado De Clientes");
		model.addAttribute("page", pageRender);
		return "admin/listarAdmin";
	}

	@GetMapping("/agregar")
	public String agregarCliente(Model model) {
		Cliente cliente = new Cliente();
		model.addAttribute("titulo", "Agregar Cliente");
		model.addAttribute("cliente", cliente);
		System.out.println("clienteGetAgregr: " + cliente);
		return "admin/agregarClienteAdmin";
	}

	@PostMapping("/agregar")
	public String procesarGuardarCliente(@Valid Cliente cliente, BindingResult result, Model model,
			SessionStatus status, RedirectAttributes flash) {

		System.out.println("MEtodo post Agregar " + cliente.toString());

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Agregar Cliente");
			return "admin/agregarClienteAdmin";
		}

		String mensaje = cliente.getId() != null ? "El usuario a sido editado exitosamente"
				: "El usuario a sido creado exitosamente";

		userDetailsService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensaje);
		return "redirect:/listar";
	}

	@GetMapping("/editar/{id}")
	public String editarCliente(@PathVariable("id") Integer id, Model model, RedirectAttributes flash,
			SessionStatus status) {
		Cliente cliente = null;
		if (id > 0) {
			cliente = clienteService.findById(id);
			if (cliente == null) {
				flash.addFlashAttribute("warning", "El cliente no existe");
				return "redirect:/listar";
			}

		} else {
			flash.addFlashAttribute("warning", "El ID del cliente no puede ser cero");
			return "redirect:/listar";
		}
		model.addAttribute("titulo", "Editar Cliente");
		model.addAttribute("cliente", cliente);
		return "admin/agregarClienteAdmin";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarCliente(@PathVariable("id") Integer id, RedirectAttributes flash) {
		if (id == 0) {
			flash.addFlashAttribute("error", "El ID no puede ser cero");
			return "redirect:/listar";
		}

		Cliente cliente = clienteService.findById(id);
		if (cliente != null) {
			clienteService.deleteById(id);
			flash.addFlashAttribute("success", "El Cliente ha sido eliminado con Éxito");
			return "redirect:/listar";
		} else {
			flash.addFlashAttribute("danger", "El Cliente no existe");
			return "redirect:/listar";
		}

	}

	@GetMapping("/info/{id}")
	public String infoCliente(Cliente cliente, Model model, RedirectAttributes flash) {
		System.out.println(" INFO:  " + cliente.getId());
		if (cliente.getId() == 0) {
			flash.addFlashAttribute("error", "Error, el cliente no existe");
			return "redirect:/listar";
		}
		cliente = clienteService.findById(cliente.getId());
		System.out.println("INFO" + cliente);
		if (cliente == null) {
			flash.addFlashAttribute("error", "Error, el cliente no existe");
			return "redirect:/listar";
		}
		model.addAttribute("titulo", "Información del Cliente");
		model.addAttribute("cliente", cliente);
		return "admin/infoClienteAdmin";
	}

	public boolean hasRole(String role) {
		SecurityContext context = SecurityContextHolder.getContext();

		if (context == null) {
			return false;
		}

		Authentication auth = context.getAuthentication();

		if (auth == null) {
			return false;
		}

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

		return authorities.contains(new SimpleGrantedAuthority(role));
	}

}
