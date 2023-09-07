package dxc.assignment.config;

import javax.security.auth.message.AuthException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

// Get exception thrown by controller and return error page
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ModelAndView handleGlobalException(Exception ex) {
		String error = "ERROR";
		String instruction = "YOU SEEM TO BE TRYING TO FIND HIS WAY HOME";

		ModelAndView modelAndView = new ModelAndView("error");
//		System.out.println("Global Error: " + ex.getMessage());
//		System.out.println("Global Error Class: " + ex.getCause().getClass());
		// Specify the error message and instruction for unauthorize access
		if (ex instanceof AuthException) {
			error = "ACCESS DENIED";
			instruction = "UNAUTHORIZE: YOU DONT HAVE THE PERMISSION TO ACCESS THIS FUNCTION";
		}

		modelAndView.addObject("error", error);
		modelAndView.addObject("instruction", instruction);

		return modelAndView;
	}
}