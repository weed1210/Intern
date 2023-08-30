package dxc.assignment.model.response;

public class AuthenticateResponse {
	private String jwtToken;

	public AuthenticateResponse(String jwtToken) {
		super();
		this.jwtToken = jwtToken;
	}
	
	public AuthenticateResponse() {
		super();
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
}
