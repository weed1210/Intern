package controller.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.util.NestedServletException;

import com.google.gson.Gson;

import dxc.assignment.controller.member.UpdateMemberController;
import dxc.assignment.helper.EncoderHelper;
import dxc.assignment.model.Member;
import dxc.assignment.model.error.ApiError;
import dxc.assignment.service.MemberService;
import helper.MemberSecurityHelper;
import retrofit2.Response;

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
	@SuppressWarnings("unchecked")
	public void testGetUpdateMemberNotFoundRedirectToIndex() throws Exception {
		Response<Member> response = mock(Response.class);
		when(response.isSuccessful()).thenReturn(false);
		when(memberService.selectById(0, "Bearer token")).thenReturn(response);

		mockMvc.perform(get("/update/0")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("authHeader", "Bearer token"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"))
				.andExpect(flash().attribute("getInfoError", "idが0のユーザーは存在しません。"))
				.andReturn();
	}

	@Test
	public void testGetUpdateMemberServerErrorRedirectToIndex() throws Exception {
		Mockito.doThrow(new IOException(""))
				.when(memberService).selectById(0, "Bearer token");

		mockMvc.perform(get("/update/0")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("authHeader", "Bearer token"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"))
				.andExpect(flash().attribute("serverError", "挿入時にエラーが発生しました。"))
				.andReturn();
	}

	@Test(expected = AuthException.class)
	@SuppressWarnings("unchecked")
	public void testGetUpdateMemberEditUpdateAdminThrowException() throws Exception {
		Response<Member> response = mock(Response.class);
		when(response.isSuccessful()).thenReturn(true);
		when(response.body()).thenReturn(MemberSecurityHelper.getValidTestAdminMember());
		when(memberService.selectById(1, "Bearer token")).thenReturn(response);

		try {
			mockMvc.perform(get("/update/1")
					.with(authentication(MemberSecurityHelper.getEditUser()))
					.sessionAttr("memberRole", "ROLE_EDIT")
					.sessionAttr("authHeader", "Bearer token"));
		} catch (NestedServletException e) {
			Exception causeEx = (Exception) e.getCause();
			throw causeEx;
		}
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testGetUpdateMemberValidRequestReturnUpdate() throws Exception {
		Member member = MemberSecurityHelper.getValidTestAdminMember();
		Response<Member> response = mock(Response.class);
		when(response.isSuccessful()).thenReturn(true);
		when(response.body()).thenReturn(member);
		when(memberService.selectById(1, "Bearer token")).thenReturn(response);

		mockMvc.perform(get("/update/1")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("authHeader", "Bearer token")
				.sessionAttr("memberRole", "ROLE_ADMIN"))
				.andExpect(status().isOk())
				.andExpect(view().name("update"))
				.andExpect(model().attribute("member", member));
	}

	@Test
	public void testPostUpdateMemberHasPasswordInvalidRequestReturnUpdate()
			throws Exception {
		Member invalidMember = MemberSecurityHelper.getDefaultTestMember();
		invalidMember.setPassword("12345678");

		mockMvc.perform(post("/update")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
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
				.with(authentication(MemberSecurityHelper.getAdminUser()))
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
				.with(authentication(MemberSecurityHelper.getAdminUser()))
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
				.with(authentication(MemberSecurityHelper.getAdminUser()))
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
				.with(authentication(MemberSecurityHelper.getAdminUser())))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"))
				.andReturn();
	}

	@Test
	public void testGetConfirmUpdateValidEditingMemberReturnConfirm() throws Exception {
		Member validTestMember = MemberSecurityHelper.getValidTestAdminMember();
		MvcResult result = mockMvc.perform(get("/confirmUpdate")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("editingMember", validTestMember))
				.andExpect(status().isOk())
				.andExpect(view().name("confirm"))
				.andReturn();

		ModelMap model = result.getModelAndView().getModelMap();
		assertThat(model.getAttribute("member"))
				.usingRecursiveComparison()
				.isEqualTo(validTestMember);
		assertEquals("会員を更新します。", model.getAttribute("title"));
		assertEquals("confirmUpdate", model.getAttribute("confirmAction"));
		assertEquals("cancelUpdate/" + validTestMember.getId(),
				model.getAttribute("cancelAction"));
	}

	@Test
	public void testGetCancelUpdateRedirectUpdate() throws Exception {
		Member validTestMember = MemberSecurityHelper.getValidTestAdminMember();
		MvcResult result = mockMvc.perform(get("/cancelUpdate/" + validTestMember.getId())
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("editingMember", validTestMember))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/update/" + validTestMember.getId()))
				.andReturn();

		HttpSession session = result.getRequest().getSession();
		Member expectedNewMember = (Member) session.getAttribute("editingMember");
		assertEquals(null, expectedNewMember);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testPostConfirmUpdateValidMemberRedirectConfirmUpdate() throws Exception {
		Member validTestMember = MemberSecurityHelper.getValidTestAdminMember();
		Response<Void> response = mock(Response.class);
		when(response.isSuccessful()).thenReturn(true);
		when(memberService.update(validTestMember, "Bearer token")).thenReturn(response);

		mockMvc.perform(post("/confirmUpdate")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("authHeader", "Bearer token")
				.flashAttr("member", validTestMember))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/success"))
				.andExpect(flash().attribute("successMessage", "更新が完了しました。"));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testPostConfirmUpdateInvalidRequestRedirectIndex() throws Exception {
		Member validTestMember = MemberSecurityHelper.getValidTestAdminMember();
		Response<Void> response = mock(Response.class, RETURNS_DEEP_STUBS);
		when(response.isSuccessful()).thenReturn(false);
		ApiError apiError = new ApiError("error", HttpStatus.BAD_REQUEST);
		Reader reader = new StringReader(new Gson().toJson(apiError));
		when(response.errorBody().charStream()).thenReturn(reader);
		when(memberService.update(validTestMember, "Bearer token")).thenReturn(response);

		mockMvc.perform(post("/confirmUpdate")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("authHeader", "Bearer token")
				.flashAttr("member", validTestMember))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/confirmUpdate"))
				.andExpect(flash().attribute("confirmError", "更新中にエラーが発生しました。"));
	}

	@Test
	public void testPostConfirmUpdateServerErrorRedirectIndex() throws Exception {
		Member validTestMember = MemberSecurityHelper.getValidTestAdminMember();
		Mockito.doThrow(new IOException(""))
				.when(memberService).update(validTestMember, "Bearer token");

		mockMvc.perform(post("/confirmUpdate")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("authHeader", "Bearer token")
				.flashAttr("member", validTestMember))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/confirmUpdate"))
				.andExpect(flash().attribute("confirmError", "サーバーに接続できません"));
	}
}
