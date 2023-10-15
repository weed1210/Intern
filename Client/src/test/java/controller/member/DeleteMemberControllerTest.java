package controller.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;

import javax.security.auth.message.AuthException;

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

import dxc.assignment.controller.member.DeleteMemberController;
import dxc.assignment.helper.EncoderHelper;
import dxc.assignment.model.Member;
import dxc.assignment.model.error.ApiError;
import dxc.assignment.service.MemberService;
import helper.MemberSecurityHelper;
import retrofit2.Response;

@RunWith(SpringRunner.class)
@WebAppConfiguration
public class DeleteMemberControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private DeleteMemberController controllerUnderTest;

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
	public void testGetConfirmUpdateNotFoundRedirectIndex() throws Exception {
		Response<Member> response = mock(Response.class);
		when(response.isSuccessful()).thenReturn(false);
		when(memberService.selectById(0, "Bearer token")).thenReturn(response);

		mockMvc.perform(get("/confirmDelete/0")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("authHeader", "Bearer token")
				.sessionAttr("memberRole", "ROLE_ADMIN"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"))
				.andExpect(flash().attribute("getInfoError", "idが0のユーザーは存在しません。"));
	}

	@Test
	public void testGetConfirmUpdateServerErrorRedirectIndex() throws Exception {
		Mockito.doThrow(new IOException())
				.when(memberService).selectById(0, "Bearer token");

		mockMvc.perform(get("/confirmDelete/0")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("authHeader", "Bearer token")
				.sessionAttr("memberRole", "ROLE_ADMIN"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testGetConfirmUpdateValidEditingMemberReturnConfirm() throws Exception {
		Member validTestMember = MemberSecurityHelper.getValidTestAdminMember();
		Response<Member> response = mock(Response.class);
		when(response.isSuccessful()).thenReturn(true);
		when(response.body()).thenReturn(validTestMember);
		when(memberService.selectById(1, "Bearer token")).thenReturn(response);

		MvcResult result = mockMvc.perform(get("/confirmDelete/1")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("authHeader", "Bearer token")
				.sessionAttr("memberRole", "ROLE_ADMIN"))
				.andExpect(status().isOk())
				.andExpect(view().name("confirm"))
				.andReturn();

		ModelMap model = result.getModelAndView().getModelMap();
		assertThat(model.getAttribute("member"))
				.usingRecursiveComparison()
				.isEqualTo(validTestMember);
		assertEquals("会員を削除します", model.getAttribute("title"));
		assertEquals("confirmDelete/" + validTestMember.getId(),
				model.getAttribute("confirmAction"));
		assertEquals("update/" + validTestMember.getId(),
				model.getAttribute("cancelAction"));
	}

	@Test(expected = AuthException.class)
	@SuppressWarnings("unchecked")
	public void testGetConfirmUpdateValidEditingMemberUnauthorizeThrowException()
			throws Exception {
		Member validTestMember = MemberSecurityHelper.getValidTestAdminMember();
		Response<Member> response = mock(Response.class);
		when(response.isSuccessful()).thenReturn(true);
		when(response.body()).thenReturn(validTestMember);
		when(memberService.selectById(1, "Bearer token")).thenReturn(response);

		try {
			mockMvc.perform(get("/confirmDelete/1")
					.with(authentication(MemberSecurityHelper.getEditUser()))
					.sessionAttr("memberRole", "ROLE_EDIT")
					.sessionAttr("authHeader", "Bearer token"));
		} catch (NestedServletException e) {
			throw (Exception) e.getCause();
		}
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testPostConfirmDeleteRedirectIndex() throws Exception {
		Response<Void> response = mock(Response.class);
		when(response.isSuccessful()).thenReturn(true);
		when(memberService.delete(1, "Bearer token")).thenReturn(response);

		mockMvc.perform(post("/confirmDelete/1")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("authHeader", "Bearer token"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/success"))
				.andExpect(flash().attribute("successMessage", "削除が完了しました。"));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testPostConfirmDeleteInvalidRequestRedirectIndex() throws Exception {
		Response<Void> response = mock(Response.class, RETURNS_DEEP_STUBS);
		when(response.isSuccessful()).thenReturn(false);
		ApiError apiError = new ApiError("error", HttpStatus.BAD_REQUEST);
		Reader reader = new StringReader(new Gson().toJson(apiError));
		when(response.errorBody().charStream()).thenReturn(reader);
		when(memberService.delete(1, "Bearer token")).thenReturn(response);

		mockMvc.perform(post("/confirmDelete/1")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("authHeader", "Bearer token"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/confirmDelete/1"))
				.andExpect(flash().attribute("confirmError", "削除中にエラーが発生しました。"));
	}

	@Test
	public void testPostConfirmUpdateServerErrorRedirectIndex() throws Exception {
		Member validTestMember = MemberSecurityHelper.getValidTestAdminMember();
		Mockito.doThrow(new IOException(""))
				.when(memberService).delete(1, "Bearer token");

		mockMvc.perform(post("/confirmDelete/1")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("authHeader", "Bearer token")
				.flashAttr("member", validTestMember))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/confirmDelete/1"))
				.andExpect(flash().attribute("confirmError", "サーバーに接続できません"));
	}
}
