package dxc.assignment.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dxc.assignment.model.Member;
import dxc.assignment.model.response.MemberSelectResponse;
import dxc.assignment.security.CustomPrincipal;
import dxc.assignment.service.MemberService;
import retrofit2.Response;

@Controller
public class HomeController {
	private final MemberService memberService;
	private int pageSize = 10;

	public HomeController(MemberService memberService) {
		this.memberService = memberService;
	}

	@GetMapping("/")
	public String index(ModelMap model,
			@RequestParam("searchString") Optional<String> searchString,
			@RequestParam("page") Optional<Integer> page, HttpSession session)
			throws IOException {
		String search = searchString.orElse("");
		int currentPage = page.orElse(1);
		Page<Member> paginatedMember;

		// Set search string to session
		session.setAttribute("searchString", search);
		// Get auth header
		String authHeader = (String) session.getAttribute("authHeader");

		try {
			// Call Api
			Response<MemberSelectResponse> response = memberService
					.select(search, currentPage, pageSize, authHeader);

			if (response.isSuccessful()) {
				// Paginate the result on success
				MemberSelectResponse members = response.body();
				paginatedMember = new PageImpl<Member>(
						members.getMembers(),
						PageRequest.of(currentPage - 1, pageSize),
						members.getTotalCount());
			} else {
				throw new IOException();
			}
		} catch (IOException e) {
			// On call to server fail
			// Empty paginated result
			paginatedMember = new PageImpl<Member>(new ArrayList<Member>());
			model.addAttribute("serverError",
					"サーバーに接続できません");
		}

		model.addAttribute("members", paginatedMember);
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
