package dxc.assignment.controller.member;

import java.nio.file.AccessDeniedException;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dxc.assignment.constant.MemberRole;
import dxc.assignment.model.Member;
import dxc.assignment.service.MemberService;

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
			HttpSession session, RedirectAttributes redirectAttributes) throws AuthException {
		// Get the current user and deleting user, check if deleting an higher level member
		String memberRole = (String) session.getAttribute("memberRole");
		Member member = memberService.selectById(id);
		if (memberRole.equals("ROLE_EDIT") && member.getRole().equals("ROLE_ADMIN")) {
			throw new AuthException();
		}
		
		if (member == null) {
			redirectAttributes.addFlashAttribute("getInfoError",
					"idが" + id + "のユーザーは存在しません。");
			return "redirect:/";
		}

		// Set the information for delete confimation page
		model.addAttribute("member", member);
		model.addAttribute("title", "会員を削除します");
		model.addAttribute("confirmAction", "confirmDelete/" + member.getId());
		model.addAttribute("cancelAction", "update/" + member.getId());
		return "confirm";
	}

	
	// Delete the member
	@PostMapping("/confirmDelete/{id}")
	public String confirmRegister(@PathVariable int id) {
		memberService.delete(id);

		return "redirect:/";
	}
}
