package co.com.boxercrossgym.auth.handler;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import co.com.boxercrossgym.entity.Cliente;
import co.com.boxercrossgym.service.JpaUserDetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	
	@Autowired
	JpaUserDetailsService userDetailsService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		
		Cliente cliente = userDetailsService.findByUsername(authentication.getName());
		SessionFlashMapManager flashMapManager = new SessionFlashMapManager();
		
		FlashMap flashMap = new FlashMap();
		
		flashMap.put("success", "Bienvenido, "  + cliente.getNombre()  + " haz iniciado Sesión con Exito");
		
		flashMapManager.saveOutputFlashMap(flashMap, request, response);
		
		
		if(authentication != null) {
			logger.info("El usuario '" + cliente.getNombre() +  "' ha iniciado sesión" );
		}
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		boolean rolUser = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).contains("ROLE_USER") && authorities.size() == 1;
		
		if(rolUser) {
			response.sendRedirect("/user/home");
		}
		super.onAuthenticationSuccess(request, response, authentication);
	}

}
