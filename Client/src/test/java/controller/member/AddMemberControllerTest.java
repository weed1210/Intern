package controller.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.google.gson.Gson;

import dxc.assignment.controller.member.AddMemberController;
import dxc.assignment.helper.EncoderHelper;
import dxc.assignment.model.Member;
import dxc.assignment.model.error.ApiError;
import dxc.assignment.service.MemberService;
import helper.MemberSecurityHelper;
import retrofit2.Response;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class AddMemberControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private AddMemberController controllerUnderTest;

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
	public void testGetRegister() throws Exception {
		MvcResult result = mockMvc.perform(get("/register")
				.with(authentication(MemberSecurityHelper.getAdminUser())))
				.andExpect(status().isOk())
				.andExpect(view().name("register"))
				.andReturn();

		ModelMap model = result.getModelAndView().getModelMap();
		Member actual = (Member) model.getAttribute("member");
		Member expected = Member.getDefault();
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	public void testPostRegisterInvalidRequestReturnRegister() throws Exception {
		mockMvc.perform(post("/register")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.flashAttr("member", Member.getDefault()))
				.andExpect(status().isOk())
				.andExpect(view().name("register"))
				.andExpect(model().attributeHasErrors("member"))
				.andReturn();
	}

	@Test
	public void testPostRegisterValidRequestRedirectConfirmRegister() throws Exception {
		Member member = MemberSecurityHelper.getValidTestAdminMember();

		MvcResult result = mockMvc.perform(post("/register")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.flashAttr("member", member)
				.sessionAttr("memberRole", "ROLE_ADMIN"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/confirmRegister"))
				.andReturn();

		HttpSession session = result.getRequest().getSession();
		Member expected = (Member) session.getAttribute("newMember");
		assertThat(member).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test(expected = AuthException.class)
	public void testPostRegisterUnauthorizeThrowException() throws Exception {
		Member member = MemberSecurityHelper.getValidTestAdminMember();

		try {
			mockMvc.perform(post("/register")
					.with(authentication(MemberSecurityHelper.getEditUser()))
					.flashAttr("member", member)
					.sessionAttr("memberRole", "ROLE_EDIT"));
		} catch (Exception e) {
			Exception causeEx = (Exception) e.getCause();
			throw causeEx;
		}
	}

	@Test
	public void testGetConfirmRegisterNoNewMemberRedirectRegister() throws Exception {
		mockMvc.perform(get("/confirmRegister")
				.with(authentication(MemberSecurityHelper.getAdminUser())))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/register"))
				.andReturn();
	}

	@Test
	public void testGetConfirmRegisterValidNewMemberReturnConfirm() throws Exception {
		Member validTestMember = MemberSecurityHelper.getValidTestAdminMember();
		MvcResult result = mockMvc.perform(get("/confirmRegister")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("newMember", validTestMember))
				.andExpect(status().isOk())
				.andExpect(view().name("confirm"))
				.andReturn();

		ModelMap model = result.getModelAndView().getModelMap();
		assertThat(model.getAttribute("member"))
				.usingRecursiveComparison()
				.isEqualTo(validTestMember);
		assertEquals("会員を登録します", model.getAttribute("title"));
		assertEquals("confirmRegister", model.getAttribute("confirmAction"));
		assertEquals("cancelRegister", model.getAttribute("cancelAction"));
	}

	@Test
	public void testGetCancelRegisterRedirectRegister() throws Exception {
		Member validTestMember = MemberSecurityHelper.getValidTestAdminMember();
		MvcResult result = mockMvc.perform(get("/cancelRegister")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("newMember", validTestMember))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/register"))
				.andReturn();

		HttpSession session = result.getRequest().getSession();
		Member expectedNewMember = (Member) session.getAttribute("newMember");
		assertEquals(null, expectedNewMember);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testPostConfirmRegisterDuplicateEmailRedirectConfirmRegister()
			throws Exception {
		Member validTestMember = MemberSecurityHelper.getValidTestAdminMember();
		Response<Void> response = mock(Response.class, RETURNS_DEEP_STUBS);
		when(response.isSuccessful()).thenReturn(false);
		ApiError apiError = new ApiError("error", HttpStatus.BAD_REQUEST);
		Reader reader = new StringReader(new Gson().toJson(apiError));
		when(response.errorBody().charStream()).thenReturn(reader);
		when(memberService.insert(validTestMember, "Bearer token"))
				.thenReturn(response);

		mockMvc.perform(post("/confirmRegister")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("authHeader", "Bearer token")
				.flashAttr("member", validTestMember))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/confirmRegister"))
				.andExpect(flash().attribute("confirmError", "挿入中にエラーが発生しました。"));
	}

	@Test
	public void testPostConfirmRegisterFailRequestRedirectConfirmRegister()
			throws Exception {
		Member validTestMember = MemberSecurityHelper.getValidTestAdminMember();
		Mockito.doThrow(new IOException(""))
				.when(memberService).insert(validTestMember, "Bearer token");

		mockMvc.perform(post("/confirmRegister")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("authHeader", "Bearer token")
				.flashAttr("member", validTestMember))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/confirmRegister"))
				.andExpect(flash().attribute("confirmError", "サーバーに接続できません"));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testPostConfirmRegisterValidMemberRedirectIndex() throws Exception {
		Member validTestMember = MemberSecurityHelper.getValidTestAdminMember();
		Response<Void> response = mock(Response.class);
		when(response.isSuccessful()).thenReturn(true);
		when(memberService.insert(validTestMember, "Bearer token"))
				.thenReturn(response);

		mockMvc.perform(post("/confirmRegister")
				.with(authentication(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("authHeader", "Bearer token")
				.flashAttr("member", validTestMember))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"));
	}
}
