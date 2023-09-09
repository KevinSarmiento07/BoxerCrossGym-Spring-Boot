package co.com.boxercrossgym.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.boxercrossgym.entity.Entrenamiento;

@Repository("iEntrenamientoDao")
public interface IEntrenamientoDao extends JpaRepository<Entrenamiento, Integer>{
    

}
