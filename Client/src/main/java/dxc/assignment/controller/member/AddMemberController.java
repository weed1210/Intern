package dxc.assignment.controller.member;

import java.io.IOException;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import dxc.assignment.constant.MemberRole;
import dxc.assignment.helper.EncoderHelper;
import dxc.assignment.helper.UIConstant;
import dxc.assignment.model.Member;
import dxc.assignment.model.error.ApiError;
import dxc.assignment.service.MemberService;
import retrofit2.Response;

@Controller
@Secured({ MemberRole.ADMIN, MemberRole.EDIT })
public class AddMemberController {
	private final MemberService memberService;
	private final EncoderHelper encoderHelper;

	public AddMemberController(MemberService memberService, EncoderHelper encoderHelper) {
		this.memberService = memberService;
		this.encoderHelper = encoderHelper;
	}

	/**
	 * Displays the registration page for creating a new member.
	 *
	 * This method prepares the registration page and sets a default Member object for binding
	 * with the registration form.
	 *
	 * @param model The ModelMap object used to add attributes for rendering the view.
	 * @return The name of the view template to be rendered ("register").
	 */
	@GetMapping("/register")
	public String register(ModelMap model) {
		// Set default member for binding
		model.addAttribute("member", Member.getDefault());
		return "register";
	}

	/**
	 * Validates member fields and redirects to the confirmation page for registration.
	 *
	 * This method handles the form submission for registering a new member. It validates the
	 * member's fields and, if validation passes, sets the new member in the session for confirmation.
	 *
	 * @param member The Member object with registration information.
	 * @param bindingResult The BindingResult object to check for validation errors.
	 * @param session The HttpSession object for storing session attributes.
	 * @param modelMap The ModelMap object used to add attributes for rendering the view in case of errors.
	 * @return A redirection to the confirmation page for registration or back to the "register" page in case of validation errors.
	 * @throws AuthException If the current user is not authorized to register a higher-level member.
	 */
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("member") Member member,
			BindingResult bindingResult, HttpSession session, ModelMap modelMap)
			throws AuthException {
		if (bindingResult.hasErrors()) {
			modelMap.addAttribute("errors", bindingResult.getAllErrors());
			return "register";
		}

		// Get the current user and new user, check if registering an higher level
		// member
		String memberRole = (String) session.getAttribute("memberRole");
		if (memberRole.equals("ROLE_EDIT") && member.getRole().equals("ROLE_ADMIN")) {
			throw new AuthException();
		}

		session.setAttribute("newMember", member);
		return "redirect:/confirmRegister";
	}

	/**
	 * Displays the confirmation page for registering a new member.
	 *
	 * This method prepares and displays the confirmation page with the new member's information
	 * before finalizing the registration.
	 *
	 * @param session The HttpSession object for retrieving the new member.
	 * @param model The ModelMap object used to add attributes for rendering the view.
	 * @return The name of the view template to be rendered ("confirm" or a redirection to the "register" page).
	 */
	@GetMapping("/confirmRegister")
	public String confirmRegister(HttpSession session, ModelMap model) {
		// Try get the member from session
		Member member = (Member) session.getAttribute("newMember");
		if (member == null) {
			return "redirect:/register";
		}

		// Set the information for confirm page
		model.addAttribute("member", member);
		model.addAttribute("title", UIConstant.ADD_MEMBER_TITLE);
		model.addAttribute("buttonConfirm", UIConstant.ADD_MEMBER_BUTTON);
		model.addAttribute("prompt", UIConstant.ADD_MEMBER_PROMPT);
		model.addAttribute("confirmAction", "confirmRegister");
		model.addAttribute("cancelAction", "cancelRegister");
		return "confirm";
	}

	/**
	 * Cancels the confirmation of the registration process.
	 *
	 * This method is called when the user cancels the confirmation of registering a new member.
	 * It removes the new member from the session and redirects back to the registration page.
	 *
	 * @param session The HttpSession object for managing session attributes.
	 * @return A redirection to the registration page.
	 */
	@GetMapping("/cancelRegister")
	public String cancelRegister(HttpSession session) {
		session.removeAttribute("newMember");

		return "redirect:/register";
	}

	/**
	 * Finalizes the registration of the new member.
	 *
	 * This method handles the confirmation of registering a new member. It encodes the new
	 * member's password, sends the insert request to the server, and handles success or failure
	 * responses accordingly.
	 *
	 * @param member The Member object with registration information.
	 * @param redirectAttributes RedirectAttributes used for passing attributes when redirecting.
	 * @param session The HttpSession object for managing session attributes.
	 * @return A redirection to the success page or back to the confirmation page in case of errors.
	 */
	@PostMapping("/confirmRegister")
	public String confirmRegister(@Valid @ModelAttribute("member") Member member,
			RedirectAttributes redirectAttributes, HttpSession session) {
		String authHeader = (String) session.getAttribute("authHeader");

		try {
			// Encode the new member password before insert
			encoderHelper.encodeMemberPassword(member);
			Response<Void> response = memberService.insert(member, authHeader);
			if (response.isSuccessful()) {
				redirectAttributes.addFlashAttribute("successMessage",
						UIConstant.ERROR_PAGE_MESSAGE);
				return "redirect:/success";
			} else {
				// On server return error
				// Get error from server response
//				ApiError error = new Gson().fromJson(
//						response.errorBody().charStream(), ApiError.class);
//				redirectAttributes.addFlashAttribute("confirmError",
//						error.getResponse());
				redirectAttributes.addFlashAttribute("confirmError",
						UIConstant.ADD_MEMBER_FAIL);
				return "redirect:/confirmRegister";
			}
		} catch (IOException e) {
			// On call to server fail
			System.out.println(e);
			redirectAttributes.addFlashAttribute("confirmError",
					UIConstant.CALL_SERVER_FAIL);
			return "redirect:/confirmRegister";
		}
	}
}
