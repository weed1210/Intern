package dxc.assignment.helper;

import retrofit2.Response;

public class AuthHelper {
	public static String checkResponseStatusToDecideDestination(Response<?> response,
			String defaultDest) {
		if (response.code() == 403) {
			return "login";
		} else {
			return defaultDest;
		}
	}
}
