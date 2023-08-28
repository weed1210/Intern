package dxc.assignment.service;

import java.io.IOException;
import java.util.Collections;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import dxc.assignment.client.MemberClient;
import dxc.assignment.mapper.MemberMapper;
import dxc.assignment.model.Member;
import dxc.assignment.model.response.MemberSelectResponse;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Service
public class MemberService {
	private int pageSize = 10;
	private final MemberMapper memberMapper;
	private final MemberClient memberClient;

	public MemberService(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("http://localhost:9090/Application/")
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		memberClient = retrofit.create(MemberClient.class);
	}

	public Page<Member> select(String searchString, int currentPage) throws IOException {
		MemberSelectResponse response = memberClient
				.select(searchString, currentPage, pageSize)
				.execute().body();

		Page<Member> paginatedMember = new PageImpl<Member>(response.getMembers(),
				PageRequest.of(currentPage - 1, pageSize),
				response.getTotalCount());
		return paginatedMember;
	}

	public Member selectById(int id) throws IOException {
		return memberClient.selectById(id).execute().body();
	}

	public Member selectByEmail(String email) throws IOException {
		return memberClient.selectByEmail(email).execute().body();
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
