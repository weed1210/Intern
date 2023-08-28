package dxc.assignment.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import dxc.assignment.service.MemberService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final MemberService memberService;

	public SecurityConfig(MemberService memberService) {
		this.memberService = memberService;
	}

	// DI the brypt encoder bean, automatically encode the password from login request when compare
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

	// DI the custom detail service
	@Bean
	public MemberDetailService springDataUserDetailsService() {
		return new MemberDetailService(memberService);
	}
}