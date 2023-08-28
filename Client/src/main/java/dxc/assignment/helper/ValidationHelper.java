package dxc.assignment.helper;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ValidationHelper {
	// Return true if the binding result only have error for a specified field
	public static boolean hasErrorOnlyForField(BindingResult bindingResult, String fieldName) {
		for (FieldError error : bindingResult.getFieldErrors()) {
			if (!error.getField().equals(fieldName)) {
				return false; // Found an error for a different field
			}
		}
		
		// If the specified field has error
		return bindingResult.hasFieldErrors(fieldName);
	}
}
