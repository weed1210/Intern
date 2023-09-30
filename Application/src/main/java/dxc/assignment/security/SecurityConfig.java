package dxc.assignment.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import dxc.assignment.mapper.MemberMapper;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private JwtAuthFilter jwtAuthFilter;
	private SecurityHelper securityHelper;
	private MemberDetailService memberDetailService;

	public SecurityConfig(JwtProvider jwtProvider,
			SecurityHelper securityHelper) {
		this.securityHelper = securityHelper;
		this.memberDetailService = new MemberDetailService(securityHelper);
		this.jwtAuthFilter = new JwtAuthFilter(jwtProvider, memberDetailService);
	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// DI the custom detail service
	@Bean
	public MemberDetailService springDataUserDetailsService() {
		return new MemberDetailService(securityHelper);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		// Set user defined detail service and password encoder
		auth.userDetailsService(new MemberDetailService(securityHelper))
				.passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors()
				.and()
				.authorizeRequests()
				.antMatchers("/auth/login").permitAll()
				.anyRequest().authenticated();

		// Not required csrf
		http.csrf().disable();

		// Add custom JwtAuthFilter
		http.addFilterBefore(jwtAuthFilter,
				UsernamePasswordAuthenticationFilter.class);
	}
}
