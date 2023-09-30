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

	// Go to the register page
	@GetMapping("/register")
	public String register(ModelMap model) {
		// Set default member for binding
		model.addAttribute("member", Member.getDefault());
		return "register";
	}

	// Validate member field and redirect to confirmation
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

	// Display the confirmation page for register
	@GetMapping("/confirmRegister")
	public String confirmRegister(HttpSession session, ModelMap model) {
		// Try get the member from session
		Member member = (Member) session.getAttribute("newMember");
		if (member == null) {
			return "redirect:/register";
		}

		// Set the information for confirm page
		model.addAttribute("member", member);
		model.addAttribute("title", "会員を登録します");
		model.addAttribute("buttonConfirm", "登録");
		model.addAttribute("prompt", "この内容でよろしければ、「登録」ボタンをクリックしてください。");
		model.addAttribute("confirmAction", "confirmRegister");
		model.addAttribute("cancelAction", "cancelRegister");
		return "confirm";
	}

	// When user cancel the confirmation of register process
	@GetMapping("/cancelRegister")
	public String cancelRegister(HttpSession session) {
		session.removeAttribute("newMember");

		return "redirect:/register";
	}

	// Insert the new member
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
						"登録が完了しました。");
				return "redirect:/success";
			} else {
				// On server return error
				// Get error from server response
//				ApiError error = new Gson().fromJson(
//						response.errorBody().charStream(), ApiError.class);
//				redirectAttributes.addFlashAttribute("confirmError",
//						error.getResponse());
				redirectAttributes.addFlashAttribute("confirmError",
						"挿入中にエラーが発生しました。");
				return "redirect:/confirmRegister";
			}
		} catch (IOException e) {
			// On call to server fail
			System.out.println(e);
			redirectAttributes.addFlashAttribute("confirmError",
					"サーバーに接続できません");
			return "redirect:/confirmRegister";
		}
	}
}
