package dxc.assignment.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private CustomAuthProvider authProvider;

	public SecurityConfig(CustomAuthProvider authProvider) {
		this.authProvider = authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		// Register user define custom auth provider
		auth.authenticationProvider(authProvider);
	}

	// DI the brypt encoder bean, automatically encode the password from login
	// request when compare
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/resources/**")
				.permitAll()
				.anyRequest().authenticated();

		http.formLogin().loginPage("/login").permitAll()
				.failureUrl("/login-error")
				.defaultSuccessUrl("/login-sucess", true);

		http.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login").deleteCookies("JSESSIONID")
				.invalidateHttpSession(true);

		// Not required csrf
		http.csrf().disable();

		http.exceptionHandling().accessDeniedPage("/accessDenied");
	}
}