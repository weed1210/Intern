package dxc.assignment.model.error;

import org.springframework.http.HttpStatus;

public class ApiError {
	private HttpStatus status;
	private String response;

	public ApiError(String response, HttpStatus status) {
		this.response = response;
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
}