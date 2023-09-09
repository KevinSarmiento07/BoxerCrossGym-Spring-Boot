package co.com.boxercrossgym.service;

import java.util.List;

import co.com.boxercrossgym.entity.Bloque;
import co.com.boxercrossgym.entity.Entrenamiento;

public interface IEntrenamientoService {


    public void save(Entrenamiento entrenamiento);


    public List<Entrenamiento> findAll();


    public Entrenamiento findById(Integer id);

    public void deleteById (Integer id);


    /** <---- Methods Bloques  */
    public void saveBloque(Bloque bloque);


    public List<Bloque> findAllBloques();

    public Bloque findBloqueById(Integer id);


    public void deleteBloqueById(Integer id);
}
