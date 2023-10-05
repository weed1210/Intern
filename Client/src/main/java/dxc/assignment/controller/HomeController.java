package dxc.assignment.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.ws.rs.ForbiddenException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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
	private int pageSize = 5;

	/**
	 * @param memberService
	 */
	public HomeController(MemberService memberService) {
		this.memberService = memberService;
	}

	/**
	 * Handles requests to the root URL ("/") of the application.
	 *
	 * This method performs a search operation based on the provided query
	 * parameters and paginates the results for display on the "index" page. Set the
	 * search string to session
	 *
	 * @param model        The ModelMap object used to add attributes for rendering
	 *                     the view.
	 * @param searchString An optional query parameter for specifying the search
	 *                     string (default: empty string).
	 * @param page         An optional query parameter for specifying the page
	 *                     number (default: 1).
	 * @param session      The HttpSession object for maintaining session data.
	 * @return The name of the view template to be rendered ("index").
	 * @throws IOException If there is an error while interacting with external
	 *                     services.
	 */
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

	/**
	 * Handles requests to the "/login" URL, displaying the login page.
	 *
	 * This method is responsible for rendering the login page when users navigate
	 * to the "/login" URL.
	 *
	 * @return The name of the view template to be rendered ("login").
	 */
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	/**
	 * Handles successful authentication and session initialization after a user
	 * logs in.
	 *
	 * This method is responsible for processing successful authentication and
	 * initializing the user's session with relevant information such as
	 * authentication tokens, email, and role.
	 *
	 * @param authentication The Authentication object containing user
	 *                       authentication details.
	 * @param session        The HttpSession object for storing session attributes.
	 * @return A redirection to the root URL ("/") after successful authentication.
	 */
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
		System.out.println("Login success - principal.getName()");

		return "redirect:/";
	}

	/**
	 * This method is responsible for processing failed login attempts and
	 * displaying an error message to the user on the login page.
	 *
	 * @param model The ModelMap object used to add attributes for rendering the
	 *              view.
	 * @return The name of the view template to be rendered ("login").
	 */
	@GetMapping("/login-error")
	public String loginError(ModelMap model) {
		System.out.println("Login fail");
		model.addAttribute("loginError", "ログインIDまたはパスワードが間違っています。");
		return "login";
	}

	/**
	 * This method is responsible for rendering a success page after the user has
	 * successfully authenticated and logged in.
	 *
	 * @return The name of the view template to be rendered ("success").
	 */
	@GetMapping("/success")
	public String success() {
		return "success";
	}
}
