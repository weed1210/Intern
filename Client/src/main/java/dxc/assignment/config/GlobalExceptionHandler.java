package dxc.assignment.config;

import javax.security.auth.message.AuthException;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import dxc.assignment.exception.ForbiddenException;
import dxc.assignment.helper.UIConstant;

// Get exception thrown by controller and return error page
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ModelAndView handleGlobalException(Exception ex) throws Exception {
		// If the exception is annotated with @ResponseStatus rethrow it and let
		// the framework handle it - like the OrderNotFoundException example
		// at the start of this post.
		// AnnotationUtils is a Spring Framework utility class.
		if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null)
			throw ex;

		ModelAndView modelAndView = new ModelAndView("error");
		String error = UIConstant.ERROR_PAGE_MESSAGE;
		String instruction = UIConstant.ERROR_PAGE_INSTRUCTION;

		modelAndView.addObject("error", error);
		modelAndView.addObject("instruction", instruction);

		return modelAndView;
	}

	@ExceptionHandler(AuthException.class)
	public ModelAndView handleAuthException(AuthException ex) {
		ModelAndView modelAndView = new ModelAndView("error");
		String error = UIConstant.AUTH_ERROR_MESSAGE;
		String instruction = UIConstant.AUTH_ERROR_INSTRUCTION;

		modelAndView.addObject("error", error);
		modelAndView.addObject("instruction", instruction);

		return modelAndView;
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(ForbiddenException.class)
	public ModelAndView handleForbiddenException(ForbiddenException ex) {
		return new ModelAndView("login");
	}
}