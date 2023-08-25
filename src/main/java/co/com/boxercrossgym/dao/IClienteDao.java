package co.com.boxercrossgym.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.com.boxercrossgym.entity.Cliente;

@Repository("iClienteDao")
public interface IClienteDao extends JpaRepository<Cliente, Integer>{
	
	@Query("select c from Cliente c where c.nombre LIKE %?1% or c.apellido LIKE %?1%")
	public List<Cliente> findAll(String termino);
	
	@Query("select c from Cliente c where c.nombre LIKE %?1%")
	public List<Cliente> findByNombre(String term);
	
	
	public Cliente findByUsername(String username);
	
	

}
