package dxc.assignment.controller.member;

import java.io.IOException;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import dxc.assignment.constant.MemberRole;
import dxc.assignment.model.Member;
import dxc.assignment.model.error.ApiError;
import dxc.assignment.service.MemberService;
import retrofit2.Response;

@Controller
@Secured({ MemberRole.ADMIN, MemberRole.EDIT })
public class DeleteMemberController {
	private final MemberService memberService;

	public DeleteMemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	// Display the confirmation page for deleting
	@GetMapping("/confirmDelete/{id}")
	public String confirmDelete(@PathVariable int id, ModelMap model,
			HttpSession session, RedirectAttributes redirectAttributes)
			throws AuthException {
		String authHeader = (String) session.getAttribute("authHeader");

		// Get the current user and deleting user, check if deleting an higher level
		// member
		String memberRole = (String) session.getAttribute("memberRole");

		try {
			Response<Member> response = memberService.selectById(id, authHeader);
			if (response.isSuccessful()) {
				Member member = response.body();
				// If member with role edit attemp to delete member with role admin
				// Member cannot be null as api with return member or 404 NOT FOUND
				if (memberRole.equals("ROLE_EDIT")
						&& member.getRole().equals("ROLE_ADMIN")) {
					throw new AuthException();
				}

				// Set the information for delete confimation page
				model.addAttribute("member", member);
				model.addAttribute("title", "会員を削除します");
				model.addAttribute("confirmAction", "confirmDelete/" + member.getId());
				model.addAttribute("cancelAction", "update/" + member.getId());
				return "confirm";
			} else {
				redirectAttributes.addFlashAttribute("getInfoError",
						"idが" + id + "のユーザーは存在しません。");
				return "redirect:/";
			}
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("serverError",
					"挿入時にエラーが発生しました。");
			return "redirect:/";
		}
	}

	// Delete the member
	@PostMapping("/confirmDelete/{id}")
	public String confirmRegister(@Valid @PathVariable int id, HttpSession session,
			RedirectAttributes redirectAttributes) {
		String authHeader = (String) session.getAttribute("authHeader");

		try {
			Response<Void> response = memberService.delete(id, authHeader);
			if (response.isSuccessful()) {
				redirectAttributes.addFlashAttribute("successMessage",
						"削除が完了しました。");
				return "redirect:/";
			} else {
				// On server return error
				// Get error from server response
//				ApiError error = new Gson().fromJson(
//						response.errorBody().charStream(), ApiError.class);
//				redirectAttributes.addFlashAttribute("confirmError",
//						error.getResponse());
				redirectAttributes.addFlashAttribute("confirmError",
						"削除中にエラーが発生しました。");
				return "redirect:/confirmDelete/" + id;
			}
		} catch (IOException e) {
			// On call to server fail
			System.out.println(e);
			redirectAttributes.addFlashAttribute("confirmError",
					"サーバーに接続できません");
			return "redirect:/confirmDelete/" + id;
		}
	}
}
