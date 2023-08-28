package dxc.assignment.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Member {
	private int id;

	@Size(max = 25, min = 1, message = "Please enter your email address correctly within 25 characters")
	private String username;

	@Email(message = "Invalid email format")
	private String email;

	@Pattern(regexp = "\\d{10}", message = "Must be entered within 10 digits")
	private String phoneNumber;

	@Size(min = 8, max = 25, message = "Password must be between 8 to 25 characters")
	private String password;
	
	private String passwordHash;
	
	private String role;

	public Member(int id, String username, String email, String phoneNumber, String passwordHash, String role) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = "";
		this.passwordHash = passwordHash;
		this.role = role;
	}

	public static Member getDefault() {
		return new Member(0, "", "", "", "", "VIEW");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
