package dxc.assignment.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dxc.assignment.model.Member;
import dxc.assignment.security.CustomPrincipal;
import dxc.assignment.service.MemberService;

@Controller
public class HomeController {
	private final MemberService memberService;

	public HomeController(MemberService memberService) {
		this.memberService = memberService;
	}

	@GetMapping("/")
	public String index(ModelMap model,
			@RequestParam("searchString") Optional<String> searchString,
			@RequestParam("page") Optional<Integer> page, HttpSession session)
			throws IOException {
		session.setAttribute("searchString", searchString.orElse(""));

		String authHeader = (String) session.getAttribute("authHeader");

		Page<Member> members = memberService
				.select(searchString.orElse(""), page.orElse(1), authHeader);
		model.addAttribute("members", members);

		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/login-sucess")
	public String authenticate(Authentication authentication,
			HttpSession session) {
		// Get current user detail
		CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
		Collection<? extends GrantedAuthority> authorities = authentication
				.getAuthorities();
		List<String> roles = new ArrayList<>();
		for (GrantedAuthority authority : authorities) {
			roles.add(authority.getAuthority());
		}

		// Set current user email and role to session
		session.setAttribute("authHeader", "Bearer " + principal.getJwtToken());
		session.setAttribute("memberEmail", principal.getName());
		session.setAttribute("memberRole", roles.get(0));

		return "redirect:/";
	}

	@GetMapping("/login-error")
	public String loginError(ModelMap model) {
		model.addAttribute("loginError", "Invalid email or password");
		return "login";
	}
}
