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

	/**
	 * Displays the confirmation page for deleting a member with a specific ID.
	 *
	 * This method retrieves member information based on the provided ID and checks if the current user
	 * has the necessary permissions to delete the member. If authorized, it prepares the deletion confirmation page.
	 *
	 * @param id The ID of the member to delete.
	 * @param model The ModelMap object used to add attributes for rendering the view.
	 * @param session The HttpSession object for maintaining session data.
	 * @param redirectAttributes RedirectAttributes used for passing attributes when redirecting.
	 * @return The name of the view template to be rendered ("confirm" or a redirection to the root URL).
	 * @throws AuthException If the current user is not authorized to delete a higher-level member.
	 * @throws IOException If there is an error while interacting with external services.
	 */
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
				model.addAttribute("buttonConfirm", "OK");
				model.addAttribute("prompt", "この内容でよろしければ、「OK」ボタンをクリックしてください。");
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

	/**
	 * Deletes a member with the specified ID after confirmation.
	 *
	 * This method handles the confirmation of member deletion. It sends the delete request to the server
	 * and handles success or failure responses accordingly.
	 *
	 * @param id The ID of the member to delete.
	 * @param session The HttpSession object for managing session attributes.
	 * @param redirectAttributes RedirectAttributes used for passing attributes when redirecting.
	 * @return A redirection to the success page or back to the confirmation page in case of errors.
	 */
	@PostMapping("/confirmDelete/{id}")
	public String confirmRegister(@Valid @PathVariable int id, HttpSession session,
			RedirectAttributes redirectAttributes) {
		String authHeader = (String) session.getAttribute("authHeader");

		try {
			Response<Void> response = memberService.delete(id, authHeader);
			if (response.isSuccessful()) {
				redirectAttributes.addFlashAttribute("successMessage",
						"削除が完了しました。");
				return "redirect:/success";
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
