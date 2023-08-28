package controller.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.security.auth.message.AuthException;

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

import dxc.assignment.controller.member.AddMemberController;
import dxc.assignment.controller.member.DeleteMemberController;
import dxc.assignment.helper.EncoderHelper;
import dxc.assignment.model.Member;
import dxc.assignment.service.MemberService;
import helper.MemberSecurityHelper;

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
	public void testGetConfirmUpdateNoEditingMemberRedirectIndex() throws Exception {
		when(memberService.selectById(0)).thenReturn(null);
		
		mockMvc.perform(get("/confirmDelete/0")
				.with(user(MemberSecurityHelper.getAdminUser()))
				.sessionAttr("memberRole", "ROLE_ADMIN"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"))
				.andReturn();
	}

	@Test
	public void testGetConfirmUpdateValidEditingMemberReturnConfirm() throws Exception {
		Member validTestMember = MemberSecurityHelper.getValidTestAdminMember();
		when(memberService.selectById(1)).thenReturn(validTestMember);
		
		MvcResult result = mockMvc.perform(get("/confirmDelete/1")
				.with(user(MemberSecurityHelper.getAdminUser()))
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
	public void testGetConfirmUpdateValidEditingMemberUnauthorizeThrowException() throws Exception {
		Member validTestMember = MemberSecurityHelper.getValidTestAdminMember();
		when(memberService.selectById(1)).thenReturn(validTestMember);
		
		try {
			mockMvc.perform(get("/confirmDelete/1")
					.with(user(MemberSecurityHelper.getEditUser()))
					.sessionAttr("memberRole", "ROLE_EDIT"));
		}
		catch (NestedServletException e) {
			throw (Exception) e.getCause();
		}
	}
	
	@Test
	public void testPostConfirmDeleteRedirectIndex() throws Exception {
		mockMvc.perform(post("/confirmDelete/1")
				.with(user(MemberSecurityHelper.getAdminUser())))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"));
	}
}
