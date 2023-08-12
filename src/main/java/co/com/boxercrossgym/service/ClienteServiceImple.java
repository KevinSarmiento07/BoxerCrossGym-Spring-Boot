package co.com.boxercrossgym.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.boxercrossgym.dao.IClienteDao;
import co.com.boxercrossgym.dao.IPagoDao;
import co.com.boxercrossgym.dao.IPlanDao;
import co.com.boxercrossgym.dao.ITipoPagoDao;
import co.com.boxercrossgym.entity.Cliente;
import co.com.boxercrossgym.entity.Pago;
import co.com.boxercrossgym.entity.Plan;
import co.com.boxercrossgym.entity.TipoPago;

@Service(value = "clienteServiceImple")
public class ClienteServiceImple implements IClienteService{

	
	@Autowired
	IClienteDao clienteDao;
	
	
	@Autowired
	IPagoDao pagoDao;
	
	@Autowired
	IPlanDao planDao;
	
	@Autowired
	ITipoPagoDao tipoPagoDao;
	
	@Override
	@Transactional
	public void save(Cliente cliente) {
		clienteDao.save(cliente);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll(String termino) {
		if(termino != null) {
			return clienteDao.findAll(termino);
		}
		return clienteDao.findAll();
	}

	@Override
	@Transactional
	public void deleteById(Integer id) {
		clienteDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findById(Integer id) {
		return clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}

	@Override
	@Transactional
	public void savePago(Pago pago) {
		pagoDao.save(pago);
	}

	@Override
	public Pago findPagoById(Integer id) {
		return pagoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Pago> findAllPagos() {
		return pagoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Pago> findAllPagos(Pageable pageable) {
		return pagoDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return clienteDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findByNombre(String term) {
		return clienteDao.findByNombre(term);
	}

	@Override
	public List<Plan> findAllPlanes() {
		return planDao.findAll();
	}

	@Override
	public List<TipoPago> findAllTipoPago() {
		return tipoPagoDao.findAll();
	}

	@Override
	public List<Pago> findByPagoVencido() {
		return pagoDao.findByPagoVencido();
	}

	@Override
	public List<Pago> findByPagoActivo() {
		return pagoDao.findByPagoActivo();
	}

	@Override
	public List<Pago> findByPagoCliente() {
		return pagoDao.findByPagoCliente();
	}

	

}
