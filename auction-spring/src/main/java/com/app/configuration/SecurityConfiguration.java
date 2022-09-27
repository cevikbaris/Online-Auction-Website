package com.app.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // Spring Security pre/post annotations.
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserAuthService userAuthService;
	
   
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.httpBasic(); //basic auth- we use it
		
		http.httpBasic().authenticationEntryPoint(new AuthEntryPoint());

		
		http.authorizeRequests()
				.antMatchers(HttpMethod.POST,"/auth").authenticated()
				.antMatchers(HttpMethod.PUT,"/users/{username}").authenticated()
				.antMatchers(HttpMethod.POST,"/auction").authenticated()
			.and()
			.authorizeRequests().anyRequest().permitAll();// bunun dışındaki istekleri kabul et
		
		
		//cookie üretip onunla bir kere login olduktan sonra her defasında auth olmuş gibi
		//istek atabiliyorduk engelledik bunu ve artık isteklerde auth yolluyoruz.
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	
	
	//config useri alıp kullanabilsin ve password encode yapabilsin diye
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userAuthService).passwordEncoder(passwordEncoder());
	}
	

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
