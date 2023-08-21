package co.com.boxercrossgym.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.boxercrossgym.dao.IClienteDao;
import co.com.boxercrossgym.entity.Cliente;
import co.com.boxercrossgym.entity.Role;


@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService, IUsuarioService {
	
	@Autowired
	private IClienteDao clienteDao;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Cliente cliente = clienteDao.findByUsername(username);
		
		if(cliente == null) {
			throw new UsernameNotFoundException("El usuario no existe en el sistema");
		}
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		for(Role role : cliente.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}
		if(authorities.isEmpty()) {
			throw new UsernameNotFoundException("Error en el login, el usuario: " + username + "no tiene roles asignados");
		}
		
		return new User(username, cliente.getPassword(), cliente.isEnabled(), true, true, true, authorities);
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		if(cliente.getId() == null) {
			cliente.setEnabled(true);
			
			cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
			
			Role role = new Role();
			
			role.setAuthority("ROLE_USER");
			
			cliente.addRole(role);
		}else {
			
			if(!cliente.getPassword().isEmpty()) {
				cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
			}else {
				
				cliente.setPassword(findByUsername(cliente.getUsername()).getPassword());
			}
		}
		
		clienteDao.save(cliente);
		
	}
	
	
	@Transactional(readOnly = true)
	public Cliente findByUsername(String username) {
		return clienteDao.findByUsername(username);
	}
	
	
	
	
	

}
