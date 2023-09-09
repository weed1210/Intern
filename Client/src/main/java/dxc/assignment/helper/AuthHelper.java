package dxc.assignment.helper;


import org.springframework.http.HttpStatus;

import dxc.assignment.exception.ForbiddenException;
import retrofit2.Response;

public class AuthHelper {
	public static <T> Response<T> checkForForbiddenError(Response<T> response) throws ForbiddenException {
		if (response.code() == HttpStatus.FORBIDDEN.value()) {
			throw new ForbiddenException();
		}
		
		return response;
	}
}
