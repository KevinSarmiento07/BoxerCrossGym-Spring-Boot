package co.com.boxercrossgym.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import co.com.boxercrossgym.dao.ITipoPagoDao;
import co.com.boxercrossgym.entity.Cliente;
import co.com.boxercrossgym.entity.Pago;
import co.com.boxercrossgym.entity.Plan;
import co.com.boxercrossgym.entity.TipoPago;

public interface IClienteService {

	
	public void save(Cliente cliente);
	
	public List<Cliente> findAll();
	
	public void deleteById(Integer id);
	
	public Cliente findById(Integer id);
	
	public Page<Cliente> findAll(Pageable pageable);
	
	public void savePago(Pago pago);
	
	
	public Pago findPagoById(Integer id);
	
	public List<Cliente> findAll(String termino);
	
	
	public List<Pago> findAllPagos();
	
	public Page<Pago> findAllPagos(Pageable pageable);
	
	
	public List<Cliente> findByNombre(String term);
	public List<Plan> findAllPlanes();
	
	
	public List<TipoPago> findAllTipoPago();
	
	
	public List<Pago> findByPagoVencido();
	
	public List<Pago> findByPagoActivo();
	
	public List<Pago> findByPagoCliente();
	
}
