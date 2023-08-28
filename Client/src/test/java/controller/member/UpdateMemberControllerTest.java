package controller.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.util.NestedServletException;

import dxc.assignment.controller.member.UpdateMemberController;
import dxc.assignment.helper.EncoderHelper;
import dxc.assignment.model.Member;
import dxc.assignment.service.MemberService;
import helper.MemberSecurityHelper;

@RunWith(SpringRunner.class)
@WebAppConfiguration
public class UpdateMemberControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private UpdateMemberController controllerUnderTest;

	@Mock
	private MemberService memberService;

	@Mock
	private EncoderHelper encoderHelper;

	@Before
	public void initTest() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/view/");
		viewResolver.setSuffix(".jsp");
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders
				.standaloneSetup(controllerUnderTest)
				.setViewResolvers(viewResolver)
				.build();
	}

	@Test
	public void testGetUpdateMemberNotExistRedirectToIndex() throws Exception {
		when(memberService.selectById(0)).thenReturn(null);

		mockMvc.perform(get("/update/0")
				.with(user(MemberSecurityHelper.getAdminUser())))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"))
				.andExpect(flash().attribute("getInfoError", "idが0のユーザーは存在しません。"))
				.andReturn();
	}

	@Test(expected = AuthException.class)
	public void testGetUpdateMemberEditUpdateAdminThrowException() throws Exception {
		when(memberService.selectById(1)).thenReturn(MemberSecurityHelper.getValidTestAdminMember());
		
		try {
			mockMvc.perform(get("/update/1")
					.with(user(MemberSecurityHelper.getEditUser()))
					.sessionAttr("memberRole", "ROLE_EDIT"));
		}
		catch (NestedServletException e) {
			Exception causeEx = (Exception) e.getCause();
			throw causeEx;
		}
	}

	@Test
	public void testGetUpdateMemberValidRequestReturnUpdate() throws Exception {
		when(memberService.selectById(1)).thenReturn(MemberSecurityHelper.getValidTestAdminMember());
		
		mockMvc.perform(get("/update/1")
				.with(user(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("memberRole", "ROLE_ADMIN"))
				.andExpect(status().isOk())
				.andExpect(view().name("update"));
	}

	@Test
	public void testPostUpdateMemberHasPasswordInvalidRequestReturnUpdate()
			throws Exception {
		Member invalidMember = MemberSecurityHelper.getDefaultTestMember();
		invalidMember.setPassword("12345678");

		mockMvc.perform(post("/update")
				.with(user(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("memberRole", "ROLE_ADMIN")
				.flashAttr("member", invalidMember))
				.andExpect(status().isOk())
				.andExpect(view().name("update"));
	}

	@Test
	public void testPostUpdateMemberHasPasswordValidRequestRedirectConfirmUpdate()
			throws Exception {
		Member member = MemberSecurityHelper.getValidTestAdminMember();

		MvcResult result = mockMvc.perform(post("/update")
				.with(user(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("memberRole", "ROLE_ADMIN")
				.flashAttr("member", member))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/confirmUpdate"))
				.andReturn();

		HttpSession session = result.getRequest().getSession();
		assertThat(session.getAttribute("editingMember"))
				.usingRecursiveComparison()
				.isEqualTo(member);
	}

	@Test
	public void testPostUpdateMemberBlankPasswordInvalidRequestReturnUpdate()
			throws Exception {
		Member invalidMember = MemberSecurityHelper.getDefaultTestMember();

		mockMvc.perform(post("/update")
				.with(user(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("memberRole", "ROLE_ADMIN")
				.flashAttr("member", invalidMember))
				.andExpect(status().isOk())
				.andExpect(view().name("update"));
	}

	@Test
	public void testPostUpdateMemberBlankPasswordValidRequestRedirectConfirmUpdate()
			throws Exception {
		Member member = MemberSecurityHelper.getValidTestAdminMember();
		member.setPassword("");

		MvcResult result = mockMvc.perform(post("/update")
				.with(user(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("memberRole", "ROLE_ADMIN")
				.flashAttr("member", member))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/confirmUpdate"))
				.andReturn();

		HttpSession session = result.getRequest().getSession();
		assertThat(session.getAttribute("editingMember"))
				.usingRecursiveComparison()
				.isEqualTo(member);
	}

	@Test
	public void testGetConfirmUpdateNoEditingMemberRedirectIndex() throws Exception {
		mockMvc.perform(get("/confirmUpdate")
				.with(user(MemberSecurityHelper.getAdminUser())))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"))
				.andReturn();
	}

	@Test
	public void testGetConfirmUpdateValidEditingMemberReturnConfirm() throws Exception {
		Member validTestMember = MemberSecurityHelper.getValidTestAdminMember();
		MvcResult result = mockMvc.perform(get("/confirmUpdate")
				.with(user(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("editingMember", validTestMember))
				.andExpect(status().isOk())
				.andExpect(view().name("confirm"))
				.andReturn();

		ModelMap model = result.getModelAndView().getModelMap();
		assertThat(model.getAttribute("member"))
				.usingRecursiveComparison()
				.isEqualTo(validTestMember);
		assertEquals("会員を編集します", model.getAttribute("title"));
		assertEquals("confirmUpdate", model.getAttribute("confirmAction"));
		assertEquals("cancelUpdate/" + validTestMember.getId(),
				model.getAttribute("cancelAction"));
	}

	@Test
	public void testGetCancelUpdateRedirectUpdate() throws Exception {
		Member validTestMember = MemberSecurityHelper.getValidTestAdminMember();
		MvcResult result = mockMvc.perform(get("/cancelUpdate/" + validTestMember.getId())
				.with(user(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("editingMember", validTestMember))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/update/" + validTestMember.getId()))
				.andReturn();

		HttpSession session = result.getRequest().getSession();
		Member expectedNewMember = (Member) session.getAttribute("editingMember");
		assertEquals(null, expectedNewMember);
	}
	
	@Test
	public void testPostConfirmUpdateValidMemberRedirectIndex() throws Exception {
		Member validTestMember = MemberSecurityHelper.getValidTestAdminMember();
		mockMvc.perform(post("/confirmUpdate")
				.with(user(MemberSecurityHelper.getAdminUser()))
				.flashAttr("member", validTestMember))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"));
	}
}
