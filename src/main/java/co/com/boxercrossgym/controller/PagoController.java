package co.com.boxercrossgym.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.com.boxercrossgym.entity.Cliente;
import co.com.boxercrossgym.entity.Pago;
import co.com.boxercrossgym.paginator.PageRender;
import co.com.boxercrossgym.service.IClienteService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/pago")
@SessionAttributes("pago")
public class PagoController {
	
	
	@Autowired
	IClienteService clienteService;
	
	@GetMapping("/listar")
	public String listarPagos(@RequestParam(defaultValue = "0", name = "page") int page ,Model model, @Param("termino") String termino) {
		if(termino != null && termino != "") {
			if(termino.equalsIgnoreCase("vencido")) {
				model.addAttribute("titulo", "Listado de Pagos");
				model.addAttribute("pagos", clienteService.findByPagoVencido());
				return "admin/listarPagos";
			}else if(termino.equalsIgnoreCase("activo")) {
				model.addAttribute("titulo", "Listado de Pagos");
				model.addAttribute("pagos", clienteService.findByPagoActivo());
				return "admin/listarPagos";
			}else if(termino.equalsIgnoreCase("nombre")) {
				model.addAttribute("titulo", "Listado de Pagos");
				model.addAttribute("pagos", clienteService.findByPagoCliente());
				return "admin/listarPagos";
			}
		}
		
		Sort sort = Sort.by(Sort.Direction.DESC, "fechaVencimiento");
		Pageable pageRequest = PageRequest.of(page, 15,sort);
		Page<Pago> pagos = clienteService.findAllPagos(pageRequest);
		PageRender<Pago> pageRender = new PageRender<>("/factura/listar", pagos);
		
		model.addAttribute("titulo", "Listado de Pagos");
		model.addAttribute("pagos", pagos);
		model.addAttribute("page", pageRender);
		return "admin/listarPagos";
	}
	
	@GetMapping(path =  {"/agregar", "/agregar/{id}"})
	public String agregarPago(Model model, @PathVariable(name = "id", required = false) Integer id) {
		if(id != null) {
			if(id > 0) {
				Cliente cliente = clienteService.findById(id);
				if(cliente != null) {
					model.addAttribute("cliente",cliente );
				}
			}
		}
		
		Pago pago = new Pago();
		model.addAttribute("titulo", "Agregar Pago");
		model.addAttribute("pago", pago);
		model.addAttribute("clientes", clienteService.findAll());
		model.addAttribute("tipoPagos", clienteService.findAllTipoPago());
		model.addAttribute("planes", clienteService.findAllPlanes());
		return "admin/agregarPago";
	}
	
	@PostMapping("/agregar")
	public String procesarGuardarPago(@Valid Pago pago,BindingResult result, @RequestParam(name = "buscar_persona", required = false) String buscarPersona, @RequestParam(name = "clienteId", required = false) Integer clienteId, Model model,
			SessionStatus status, RedirectAttributes flash) {
		
		System.out.println("EL CLIENTE " + pago.getCliente());
		
		if(result.hasErrors()) {
			System.out.println(result.getAllErrors()); 
			System.out.println("entro en haserrors");
			model.addAttribute("titulo", "Agregar Pago");
			model.addAttribute("tipoPagos", clienteService.findAllTipoPago());
			model.addAttribute("planes", clienteService.findAllPlanes());
			return "admin/agregarPago";
		}
		if(clienteId == null) {
			flash.addFlashAttribute("error", "Debe seleccionar un cliente");
			model.addAttribute("titulo", "Agregar Pago");
			model.addAttribute("tipoPagos", clienteService.findAllTipoPago());
			model.addAttribute("planes", clienteService.findAllPlanes());
			return "redirect:/pago/agregar";
		}
		Cliente cliente = clienteService.findById(clienteId);
		
		if(cliente == null) {
			flash.addFlashAttribute("error", "El cliente a facturar es nulo o no existe");
			return "admin/agregarPago";
		}
		pago.setCliente(cliente);
		
		
		String mensajeFlash = pago.getId() == null? "El pago a sido creado con exito" : "El pago a sido editado con exito";
		clienteService.savePago(pago);
		flash.addFlashAttribute("success", mensajeFlash);
		status.setComplete();
		System.out.println(pago.toString());
		return "redirect:/pago/listar";
	}
	
	
	@GetMapping("/editar/{id}")
	public String editarPago(@PathVariable("id") Integer id, Model model, RedirectAttributes flash) {
		
		Pago pago = null;
		
		if(id > 0) {
			pago = clienteService.findPagoById(id);
			
			if(pago == null) {
				flash.addFlashAttribute("info", "Error, cliente buscado no existe");
				return "redirect:/listar";
			}
		}else {
			flash.addFlashAttribute("error", "Error, cliente buscado no existe");
			return "redirect:/listar";
		}
		model.addAttribute("titulo", "Editar Pago");
		model.addAttribute("pago", pago);
		model.addAttribute("tipoPagos", clienteService.findAllTipoPago());
		model.addAttribute("planes", clienteService.findAllPlanes());
		return "admin/agregarPago";
	}
	
	@GetMapping(path =  "/buscar-cliente/{term}", produces = {"application/json"})
    public @ResponseBody List<Cliente> buscarClientes(@PathVariable("term") String term) {
        return clienteService.findAll(term);
    }
	

	
	
	
}
