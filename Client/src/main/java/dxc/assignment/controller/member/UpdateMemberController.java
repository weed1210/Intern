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

import com.google.gson.Gson;

import dxc.assignment.constant.MemberRole;
import dxc.assignment.helper.EncoderHelper;
import dxc.assignment.helper.ValidationHelper;
import dxc.assignment.model.Member;
import dxc.assignment.model.error.ApiError;
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

	// Go to the update page of member with id
	@GetMapping("/update/{id}")
	public String update(@PathVariable int id, ModelMap model, HttpSession session,
			RedirectAttributes redirectAttributes)
			throws AuthException, IOException {
		String authHeader = (String) session.getAttribute("authHeader");
		// Get the current user and updating user, check if updating an higher level
		// member
		String memberRole = (String) session.getAttribute("memberRole");
		Member member = memberService.selectById(id, authHeader);
		// If member not exist redirect to index and display toast
		if (member == null) {
			redirectAttributes.addFlashAttribute("getInfoError",
					"idが" + id + "のユーザーは存在しません。");
			return "redirect:/";
		}

		if (memberRole.equals("ROLE_EDIT") && member.getRole().equals("ROLE_ADMIN")) {
			throw new AuthException();
		}

		model.addAttribute("member", member);

		return "update";
	}

	// Validate member field and redirect to confirmation
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

	// Display the confirmation page for updating member with id
	@GetMapping("/confirmUpdate")
	public String confirmUpdate(HttpSession session, ModelMap model) {
		// Try get the member from session
		Member member = (Member) session.getAttribute("editingMember");
		if (member == null) {
			return "redirect:/";
		}

		// Set the information for update page
		model.addAttribute("member", member);
		model.addAttribute("title", "会員を編集します");
		model.addAttribute("confirmAction", "confirmUpdate");
		model.addAttribute("cancelAction", "cancelUpdate/" + member.getId());
		return "confirm";
	}

	// When user cancel the confirmation of updating process
	@GetMapping("/cancelUpdate/{id}")
	public String cancelUpdate(@PathVariable int id, HttpSession session) {
		session.removeAttribute("editingMember");
		return "redirect:/update/" + id;
	}

	// Update the member
	@PostMapping("/confirmUpdate")
	public String confirmUpdate(@ModelAttribute("member") Member member,
			HttpSession session, RedirectAttributes redirectAttributes) {
		String authHeader = (String) session.getAttribute("authHeader");

		// Encode the new member password before update
		try {
			encoderHelper.encodeMemberPassword(member);
			Response<Void> response = memberService.update(member, authHeader);
			if (!response.isSuccessful()) {
				// On server return error
				// Get error from server response
				ApiError error = new Gson().fromJson(
						response.errorBody().charStream(), ApiError.class);
				redirectAttributes.addFlashAttribute("confirmError",
						error.getResponse());
				return "redirect:/confirmUpdate";
			} else {
				redirectAttributes.addFlashAttribute("successMessage",
						"更新が完了しました。");
			}

			return "redirect:/";
		} catch (IOException e) {
			// On call to server fail
			System.out.println(e);
			redirectAttributes.addFlashAttribute("confirmError",
					"挿入時にエラーが発生しました。");
			return "redirect:/confirmUpdate";
		}
	}
}
