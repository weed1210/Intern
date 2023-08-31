package dxc.assignment.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dxc.assignment.model.request.LoginRequest;
import dxc.assignment.model.response.AuthenticateResponse;
import dxc.assignment.security.JwtProvider;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private AuthenticationManager authenticationManager;
	private JwtProvider jwtProvider;

	public AuthController(AuthenticationManager authenticationManager,
			JwtProvider jwtProvider) {
		this.authenticationManager = authenticationManager;
		this.jwtProvider = jwtProvider;
	}

	@PostMapping("login")
	public AuthenticateResponse authenticateUser(@RequestBody LoginRequest loginRequest) {

		// Authenticate with email and password
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getEmail(),
						loginRequest.getPassword()));

		// If not exception throw all information is valid
		// Set authentication info to Security Context
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Return jwt to client
		String jwt = jwtProvider
				.generateToken((UserDetails) authentication.getPrincipal());
		return new AuthenticateResponse(jwt);
	}
}
