package co.com.boxercrossgym.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.com.boxercrossgym.entity.Pago;

@Repository
public interface IPagoDao extends JpaRepository<Pago, Integer>{

	@Query("select p from Pago p where fechaVencimiento < CURRENT_DATE order by fechaVencimiento desc")
	public List<Pago> findByPagoVencido();
	
	@Query("select p from Pago p where fechaVencimiento > CURRENT_DATE order by fechaVencimiento desc")
	public List<Pago> findByPagoActivo();
	
	
	@Query("select p from Pago p join p.cliente c order by c.nombre ")
	public List<Pago> findByPagoCliente();
}
