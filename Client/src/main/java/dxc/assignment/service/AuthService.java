package dxc.assignment.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import dxc.assignment.client.AuthClient;
import dxc.assignment.model.request.LoginRequest;
import dxc.assignment.model.response.AuthenticateResponse;
import retrofit2.Response;
import retrofit2.Retrofit;

@Service
public class AuthService {
	private AuthClient authClient;

	public AuthService(Retrofit retrofit) {
		authClient = retrofit.create(AuthClient.class);
	}

	public Response<AuthenticateResponse> authenticate(LoginRequest loginRequest) throws IOException {
		return authClient.authenticate(loginRequest).execute();
	}
}
