package dxc.assignment.dao;

import dxc.assignment.model.request.LoginRequest;
import dxc.assignment.model.response.AuthenticateResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthClient {
	@POST("auth/login")
	Call<AuthenticateResponse> authenticate(@Body LoginRequest loginRequest);
}
