package dxc.assignment.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;

import dxc.assignment.mapper.MemberMapper;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//	@Autowired
	private MemberMapper memberMapper;
	private JwtAuthFilter jwtAuthFilter;
	private MemberDetailService memberDetailService;

	public SecurityConfig(MemberMapper memberMapper, JwtProvider jwtProvider) {
		this.memberMapper = memberMapper;
		this.memberDetailService = new MemberDetailService(memberMapper);
		this.jwtAuthFilter = new JwtAuthFilter(jwtProvider, memberDetailService);
	}

//	@Bean
//	public JwtAuthFilter jwtAuthFilter() {
//		return new JwtAuthFilter();
//	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		// Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
		return new BCryptPasswordEncoder();
	}

	// DI the custom detail service
	@Bean
	public MemberDetailService springDataUserDetailsService() {
		return new MemberDetailService(memberMapper);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(new MemberDetailService(memberMapper)) // Cung cáp
																		// userservice cho
																		// spring security
				.passwordEncoder(passwordEncoder()); // cung cấp password encoder
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
