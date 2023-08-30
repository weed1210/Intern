package dxc.assignment.service;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import dxc.assignment.client.MemberClient;
import dxc.assignment.model.Member;
import dxc.assignment.model.response.MemberSelectResponse;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Service
public class MemberService {
	private int pageSize = 10;
	private MemberClient memberClient;

	public MemberService(Retrofit retrofit) {
		this.memberClient = retrofit.create(MemberClient.class);
	}

	public Page<Member> select(String searchString, int currentPage, String authHeader) throws IOException {
		MemberSelectResponse response = memberClient
				.select(searchString, currentPage, pageSize, authHeader)
				.execute().body();

		Page<Member> paginatedMember = new PageImpl<Member>(response.getMembers(),
				PageRequest.of(currentPage - 1, pageSize),
				response.getTotalCount());
		return paginatedMember;
	}

	public Member selectById(int id) throws IOException {
		return memberClient.selectById(id).execute().body();
	}

	public Member selectByEmail(String email, String authHeader) throws IOException {
		return memberClient.selectByEmail(email, authHeader).execute().body();
	}

	public void insert(Member member) throws IOException {
		memberClient.insert(member).execute();
	}

	public void update(Member member) throws IOException {
		memberClient.update(member).execute();
	}

	public void delete(int id) throws IOException {
		memberClient.delete(id).execute();
	}
}
