package co.com.boxercrossgym.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.boxercrossgym.dao.IBloqueDao;
import co.com.boxercrossgym.dao.IEntrenamientoDao;
import co.com.boxercrossgym.entity.Bloque;
import co.com.boxercrossgym.entity.Entrenamiento;

@Service("entrenaminetoServiceImple")
public class EntrenamientoServiceImple implements IEntrenamientoService{

    @Autowired
    private IEntrenamientoDao entrenamientoDao;

    @Autowired
    private IBloqueDao bloqueDao;

	@Override
	public void save(Entrenamiento entrenamiento) {
        entrenamientoDao.save(entrenamiento);
		
	}

	@Override
	public List<Entrenamiento> findAll() {
		return entrenamientoDao.findAll();
	}

	@Override
	public Entrenamiento findById(Integer id) {
		return entrenamientoDao.findById(id).orElse(null);
	}

	@Override
	public void deleteById(Integer id) {
		entrenamientoDao.deleteById(id);
	}

	@Override
	public void saveBloque(Bloque bloque) {
		bloqueDao.save(bloque);
		
	}

	@Override
	public List<Bloque> findAllBloques() {
		return bloqueDao.findAll();
	}

	@Override
	public Bloque findBloqueById(Integer id) {
		return bloqueDao.findById(id).orElse(null);
	}

	@Override
	public void deleteBloqueById(Integer id) {
        bloqueDao.deleteById(id);
	}
    
}
