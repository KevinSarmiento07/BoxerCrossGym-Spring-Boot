package co.com.boxercrossgym.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.boxercrossgym.entity.Bloque;

@Repository("iBloqueDao")
public interface IBloqueDao extends JpaRepository<Bloque, Integer> {
    
}
