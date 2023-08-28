package controller.home;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import config.TestConfig;
import dxc.assignment.controller.HomeController;
import dxc.assignment.model.Member;
import dxc.assignment.security.SecurityConfig;
import dxc.assignment.service.MemberService;
import helper.MemberSecurityHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SecurityConfig.class, TestConfig.class })
public class HomeControllerTest {
	private MockMvc mockMvc;

	@Autowired
	FilterChainProxy springSecurityFilterChain;

	@InjectMocks
	private HomeController controllerUnderTest;

	@Mock
	private MemberService memberService;

	@Before
	public void initTest() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/view/");
		viewResolver.setSuffix(".jsp");
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders
				.standaloneSetup(controllerUnderTest)
				.setViewResolvers(viewResolver)
				.apply(springSecurity(springSecurityFilterChain))
				.build();
	}

	@Test
	public void testGetIndex() throws Exception {
		Page<Member> members = new PageImpl<Member>(new ArrayList<Member>());
		// Add some mock data to members list
		when(memberService.select("", 1)).thenReturn(members);

		mockMvc.perform(get("/")
				.with(user(MemberSecurityHelper.getAdminUser()))
				.queryParam("searchString", ""))
				.andExpect(status().isOk())
				.andExpect(view().name("index"))
				.andExpect(model().attributeExists("members"));
	}

	@Test
	public void testGetLogin() throws Exception {
		mockMvc.perform(get("/login"))
				.andExpect(status().isOk())
				.andExpect(view().name("login"));
	}

	@Test
	public void testAuthenticate() throws Exception {
		MvcResult result = mockMvc.perform(get("/login-sucess")
				.with(user(MemberSecurityHelper.getAdminUser())))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"))
				.andReturn();

		HttpSession session = result.getRequest().getSession();

		String memberEmail = (String) session.getAttribute("memberEmail");
		String memberRole = (String) session.getAttribute("memberRole");

		assertEquals("caovy@gmail.com", memberEmail);
		assertEquals("ROLE_ADMIN", memberRole);
	}

	@Test
	public void testLoginError() throws Exception {
		MvcResult result = mockMvc.perform(get("/login-error"))
				.andExpect(status().isOk())
				.andExpect(view().name("login"))
				.andReturn();

		ModelMap model = result.getModelAndView().getModelMap();
		assertEquals("Invalid email or password", model.getAttribute("loginError"));
	}
}
