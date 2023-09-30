package dxc.assignment.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collector;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import dxc.assignment.mapper.MemberMapper;
import dxc.assignment.model.Member;

public class MemberDetailService implements UserDetailsService {
	private final SecurityHelper securityHelper;

	public MemberDetailService(SecurityHelper securityHelper) {
		this.securityHelper = securityHelper;
	}

	// Get the valid user from db for authentication
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		try {
			ArrayList<Member> members = securityHelper.readFileForSuperUser(username);
			Member member = securityHelper.findMemberByEmail(members, username);

			if (member == null) {
				System.out.println("User not found");
				throw new UsernameNotFoundException("User not found");
			}

			// Return the user detail which is set until logout
			return User.builder()
					.username(member.getEmail())
					.password(member.getPasswordHash()) // Encoded
					.authorities(member.getRole())
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Read file fail");
			throw new UsernameNotFoundException("Cannot find user from file");
		}
	}

	
}
