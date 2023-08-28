package dxc.assignment.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import dxc.assignment.model.Member;
import dxc.assignment.service.MemberService;

public class MemberDetailService implements UserDetailsService {
	private final MemberService memberService;

	public MemberDetailService(MemberService memberService) {
		this.memberService = memberService;
	}

	// Get the valid user from db for authentication
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Member member = memberService.selectByEmail(username);
		if (member == null) {
			throw new UsernameNotFoundException("User not found");
		}
		
		// Return the user detail which is set until logout
		return User.builder()
				.username(member.getEmail())
				.password(member.getPasswordHash()) // Encoded
				.authorities(member.getRole())
				.build();
	}
}
