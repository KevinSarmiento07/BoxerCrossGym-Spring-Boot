package co.com.boxercrossgym.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.boxercrossgym.entity.TipoPago;

@Repository
public interface ITipoPagoDao extends JpaRepository<TipoPago, Integer>{

}
