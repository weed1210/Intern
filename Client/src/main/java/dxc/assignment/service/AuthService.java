package dxc.assignment.service;

import static org.assertj.core.api.Assertions.catchException;

import java.io.IOException;

import org.springframework.stereotype.Service;

import dxc.assignment.client.AuthClient;
import dxc.assignment.client.MemberClient;
import dxc.assignment.model.request.LoginRequest;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
