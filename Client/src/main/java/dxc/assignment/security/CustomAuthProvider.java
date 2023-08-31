package dxc.assignment.security;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import dxc.assignment.model.Member;
import dxc.assignment.model.request.LoginRequest;
import dxc.assignment.model.response.AuthenticateResponse;
import dxc.assignment.service.AuthService;
import dxc.assignment.service.MemberService;
import retrofit2.Response;


// Custom provider to login with api with email and password from login form
@Component
public class CustomAuthProvider implements AuthenticationProvider {
	private AuthService authService;
	private MemberService memberService;

	public CustomAuthProvider(AuthService authService, MemberService memberService) {
		this.authService = authService;
		this.memberService = memberService;
	}

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		String email = authentication.getName();
		String password = authentication.getCredentials().toString();

		try {
			// Call api for jwt with email and password
			String jwtToken;
			Response<AuthenticateResponse> authResponse = authService
					.authenticate(new LoginRequest(email, password));
			if (authResponse.isSuccessful()) {
				jwtToken = authResponse.body().getJwtToken();
			} else {
				return null;
			}

			// Call api for member with email
			Response<Member> memberResponse = memberService.selectByEmail(email,
					"Bearer " + jwtToken);
			if (memberResponse.isSuccessful()) {
				Member member = memberResponse.body();
				UserDetails user = User.builder()
						.username(member.getEmail())
						.password(member.getPassword())
						.authorities(member.getRole())
						.build();

				// use the credentials
				// and authenticate against the third-party system
				return new UsernamePasswordAuthenticationToken(
						new CustomPrincipal(email, jwtToken), password,
						user.getAuthorities());
			} else {
				return null;
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
