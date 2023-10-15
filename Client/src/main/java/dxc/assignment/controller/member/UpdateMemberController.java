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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dxc.assignment.constant.MemberRole;
import dxc.assignment.controller.HomeController;
import dxc.assignment.helper.AuthHelper;
import dxc.assignment.helper.EncoderHelper;
import dxc.assignment.helper.UIConstant;
import dxc.assignment.helper.ValidationHelper;
import dxc.assignment.model.Member;
import dxc.assignment.service.MemberService;
import retrofit2.Response;

@Controller
@Secured({ MemberRole.ADMIN, MemberRole.EDIT })
public class UpdateMemberController {
	private final MemberService memberService;
	private final EncoderHelper encoderHelper;

	public UpdateMemberController(MemberService memberService,
			EncoderHelper encoderHelper) {
		this.memberService = memberService;
		this.encoderHelper = encoderHelper;
	}

	/**
	 * Displays the update page for a member with a specific ID.
	 *
	 * This method retrieves member information based on the provided ID and checks if the current user
	 * has the necessary permissions to update the member's details. If authorized, it prepares the
	 * update page with the member's information.
	 *
	 * @param id The ID of the member to update.
	 * @param model The ModelMap object used to add attributes for rendering the view.
	 * @param session The HttpSession object for maintaining session data.
	 * @param redirectAttributes RedirectAttributes used for passing attributes when redirecting.
	 * @return The name of the view template to be rendered ("update" or a redirection to the root URL).
	 * @throws AuthException If the current user is not authorized to update a higher-level member.
	 * @throws IOException If there is an error while interacting with external services.
	 */
	@GetMapping("/update/{id}")
	public String update(@PathVariable int id, ModelMap model, HttpSession session,
			RedirectAttributes redirectAttributes)
			throws AuthException, IOException {
		String authHeader = (String) session.getAttribute("authHeader");
		// Get the current user and updating user, check if updating an higher level
		// member
		String memberRole = (String) session.getAttribute("memberRole");
		try {
			Response<Member> response = memberService.selectById(id, authHeader);
			if (response.isSuccessful()) {
				// Member cannot be null as api with return member or 404 NOT FOUND
				Member member = response.body();
				if (memberRole.equals("ROLE_EDIT")
						&& member.getRole().equals("ROLE_ADMIN")) {
					throw new AuthException();
				}
				model.addAttribute("member", member);
				return "update";
			} else {
				redirectAttributes.addFlashAttribute("getInfoError",
						"idが" + id + "のユーザーは存在しません。");
				return "redirect:/";
			}
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("serverError",
					UIConstant.CALL_SERVER_FAIL);
			return "redirect:/";
		}
	}

	/**
	 * Validates member fields and redirects to the confirmation page for updating.
	 *
	 * This method handles the form submission for updating a member. It validates the member's fields
	 * and, if validation passes, sets the member in the session for confirmation.
	 *
	 * @param member The Member object with updated information.
	 * @param bindingResult The BindingResult object to check for validation errors.
	 * @param session The HttpSession object for storing session attributes.
	 * @return A redirection to the confirmation page for updating or back to the "update" page in case of validation errors.
	 */
	@PostMapping("/update")
	public String update(@Valid @ModelAttribute("member") Member member,
			BindingResult bindingResult, HttpSession session) {
		// Ignore password validation when password is blank
		if (member.getPassword().isBlank()) {
			// Check if there are errors in other field beside password
			if (!ValidationHelper.hasErrorOnlyForField(bindingResult, "password")) {
				System.out.println("1 Error");
				return "update";
			}
		} else if (bindingResult.hasErrors()) {
			System.out.println("2 Error");
			return "update";
		}

		// Set the user for the delete and update confirmation page
		session.setAttribute("editingMember", member);
		return "redirect:/confirmUpdate";
	}

	/**
	 * Displays the confirmation page for updating a member.
	 *
	 * This method prepares and displays the confirmation page with the member's information before
	 * finalizing the update.
	 *
	 * @param session The HttpSession object for retrieving the editing member.
	 * @param model The ModelMap object used to add attributes for rendering the view.
	 * @return The name of the view template to be rendered ("confirm" or a redirection to the root URL).
	 */
	@GetMapping("/confirmUpdate")
	public String confirmUpdate(HttpSession session, ModelMap model) {
		// Try get the member from session
		Member member = (Member) session.getAttribute("editingMember");
		if (member == null) {
			return "redirect:/";
		}

		// Set the information for update page
		model.addAttribute("member", member);
		model.addAttribute("title", UIConstant.UPDATE_MEMBER_TITLE);
		model.addAttribute("buttonConfirm", UIConstant.UPDATE_MEMBER_BUTTON);
		model.addAttribute("prompt", UIConstant.UPDATE_MEMBER_PROMPT);
		model.addAttribute("confirmAction", "confirmUpdate");
		model.addAttribute("cancelAction", "cancelUpdate/" + member.getId());
		return "confirm";
	}

	/**
	 * Cancels the confirmation of the updating process.
	 *
	 * This method is called when the user cancels the confirmation of updating a member's information.
	 * It removes the editing member from the session and redirects back to the update page.
	 *
	 * @param id The ID of the member whose update confirmation is being canceled.
	 * @param session The HttpSession object for managing session attributes.
	 * @return A redirection to the update page for the specified member ID.
	 */
	@GetMapping("/cancelUpdate/{id}")
	public String cancelUpdate(@PathVariable int id, HttpSession session) {
		session.removeAttribute("editingMember");
		return "redirect:/update/" + id;
	}

	/**
	 * Finalizes the update of the member's information.
	 *
	 * This method handles the confirmation of updating a member's information. It encodes the new
	 * member's password, sends the update request to the server, and handles success or failure
	 * responses accordingly.
	 *
	 * @param member The Member object with updated information.
	 * @param session The HttpSession object for managing session attributes.
	 * @param redirectAttributes RedirectAttributes used for passing attributes when redirecting.
	 * @return A redirection to the success page or back to the confirmation page in case of errors.
	 */
	@PostMapping("/confirmUpdate")
	public String confirmUpdate(@ModelAttribute("member") Member member,
			HttpSession session, RedirectAttributes redirectAttributes) {
		String authHeader = (String) session.getAttribute("authHeader");

		// Encode the new member password before update
		try {
			encoderHelper.encodeMemberPassword(member);
			Response<Void> response = memberService.update(member, authHeader);
			if (response.isSuccessful()) {
				redirectAttributes.addFlashAttribute("successMessage",
						UIConstant.UPDATE_MEMBER_SUCCESS);
				return "redirect:/success";
			} else {
				redirectAttributes.addFlashAttribute("confirmError",
						UIConstant.UPDATE_MEMBER_FAIL);
				return "redirect:/confirmUpdate";
			}
		} catch (IOException e) {
			// On call to server fail
			System.out.println(e);
			redirectAttributes.addFlashAttribute("confirmError",
					UIConstant.CALL_SERVER_FAIL);
			return "redirect:/confirmUpdate";
		}
	}
}
