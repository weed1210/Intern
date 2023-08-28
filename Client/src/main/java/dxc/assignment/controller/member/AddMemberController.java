package dxc.assignment.controller.member;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import dxc.assignment.constant.MemberRole;
import dxc.assignment.helper.EncoderHelper;
import dxc.assignment.model.Member;
import dxc.assignment.service.MemberService;

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
			BindingResult bindingResult, HttpSession session) throws AuthException {
		System.out.println(member.getUsername().length());
		if (bindingResult.hasErrors()) {
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
	public String confirmRegister(@ModelAttribute("member") Member member,
			ModelMap modelMap) {
		try {
			// Encode the new member password before insert
			encoderHelper.encodeMemberPassword(member);
			System.out.println(member.getEmail());
			memberService.insert(member);

			return "redirect:/";
		} catch (DuplicateKeyException e) {
			modelMap.addAttribute("registerError",
					"メールアドレス" + member.getEmail() + "はすでに存在しています");
			return "register";
		} catch (Exception e) {
			System.out.println(e.getClass().getCanonicalName());
			System.out.println(e.getMessage());
			return "register";
		}
	}
}
