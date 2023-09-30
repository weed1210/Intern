package dxc.assignment.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.stereotype.Service;

import dxc.assignment.model.Member;

@Service
public class SecurityHelper {
	public ArrayList<Member> readFileForSuperUser(String email)
			throws URISyntaxException, FileNotFoundException {
		File file = new File(this.getClass().getResource("/login.txt").toURI());
		ArrayList<Member> members = new ArrayList<Member>();
		Scanner myReader = new Scanner(file);
		while (myReader.hasNextLine()) {
			String data = myReader.nextLine();
			members.add(convertDataToUserDetails(data));
		}
		myReader.close();
		return members;
	}

	private Member convertDataToUserDetails(String data) {
		String[] fields = data.split(" ");
		String email = fields[0];
		String password = fields[1];
		String role = fields[2];
		return new Member(0, "", email, "", password, role);
	}

	public Member findMemberByEmail(ArrayList<Member> members, String email) {
		Optional<Member> member = members.stream()
				.filter(x -> x.getEmail().equals(email))
				.findFirst();
		return member.orElse(null);
	}
}
