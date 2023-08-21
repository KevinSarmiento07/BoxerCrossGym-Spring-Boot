package co.com.boxercrossgym;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive;

import co.com.boxercrossgym.auth.handler.LoginSuccessHandler;
import co.com.boxercrossgym.service.JpaUserDetailsService;


@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig{

	
	@Autowired
	private LoginSuccessHandler successHandler;
	@Autowired
	private JpaUserDetailsService userDetailsService;
	
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http)throws Exception {
		System.out.println("ENTRO EN FILERCHAIN");
		http.authorizeHttpRequests((authz) -> {
			try {
				authz.requestMatchers("/css/**", "/js/**", "/images/**", "/adminlte/**", "/fontawesome/**", "/agregar/**").permitAll()
				/*.requestMatchers("/listar/**", "/").hasRole("USER")*/
				.requestMatchers("/uploads/**").hasRole("USER")
				.requestMatchers("/eliminar/**").hasRole("ADMIN")
				.requestMatchers("/agregar/**").hasRole("ADMIN")
				.requestMatchers("/editar/**").hasRole("ADMIN")
				.requestMatchers("/info/**").hasRole("ADMIN")
				.requestMatchers("/ver/**").hasRole("ADMIN")
				.requestMatchers("/prueba/**").hasRole("ADMIN")
				.anyRequest().authenticated();
			}catch (Exception e) {
				System.out.println("ENTRÓ EN EXCEPTION DE FILTERCHAIN");
				e.printStackTrace();
			}
		}).formLogin(login -> login.loginPage("/login").successHandler(successHandler).permitAll())
		.logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll().addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(Directive.COOKIES))))
		.exceptionHandling(((exceptionHandling) -> exceptionHandling.accessDeniedPage("/error_403")));
			return http.build();
		}
		
	 @Autowired
	    public void configureGlobal(AuthenticationManagerBuilder build) throws Exception {
	 
	        build.userDetailsService(userDetailsService)
	        .passwordEncoder(passwordEncoder);
	    }
	}
