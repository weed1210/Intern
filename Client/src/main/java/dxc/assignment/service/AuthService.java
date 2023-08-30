package dxc.assignment.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import dxc.assignment.client.AuthClient;
import dxc.assignment.model.request.LoginRequest;
import retrofit2.Retrofit;

@Service
public class AuthService {
	private AuthClient authClient;

	public AuthService(Retrofit retrofit) {
		authClient = retrofit.create(AuthClient.class);
	}

	public String authenticate(LoginRequest loginRequest) throws IOException {
		return authClient.authenticate(loginRequest).execute().body().getJwtToken();
	}
}
