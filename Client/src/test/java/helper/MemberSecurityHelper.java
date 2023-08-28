package helper;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import dxc.assignment.model.Member;

public class MemberSecurityHelper {
	public static UserDetails getAdminUser() {
		UserDetails admin = User.builder()
				.username("caovy@gmail.com")
				.password("12345678")
				.authorities("ROLE_ADMIN")
				.build();

		return admin;
	}

	public static UserDetails getEditUser() {
		UserDetails edit = User.builder()
				.username("thanhDung@gmail.com")
				.password("12345678")
				.authorities("ROLE_EDIT")
				.build();

		return edit;
	}

	public static UserDetails getViewUser() {
		UserDetails view = User.builder()
				.username("dkNhien@gmail.com")
				.password("12345678")
				.authorities("ROLE_VIEW")
				.build();

		return view;
	}

	public static Member getDefaultTestMember() {
		return Member.getDefault();
	}

	public static Member getValidTestAdminMember() {
		Member member = new Member(0,
				"name",
				"email@gmail.com",
				"1234567890",
				"",
				"ROLE_ADMIN");
		member.setPassword("12345678");
		return member;
	}
}
