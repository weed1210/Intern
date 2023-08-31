package dxc.assignment.service;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import dxc.assignment.client.MemberClient;
import dxc.assignment.model.Member;
import dxc.assignment.model.response.MemberSelectResponse;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

@Service
public class MemberService {
	private MemberClient memberClient;

	public MemberService(Retrofit retrofit) {
		this.memberClient = retrofit.create(MemberClient.class);
	}

	public Response<MemberSelectResponse> select(String searchString, int currentPage,
			int pageSize,
			String authHeader)
			throws IOException {
		return memberClient
				.select(searchString, currentPage, pageSize, authHeader)
				.execute();
	}

	public Member selectById(int id, String authHeader) throws IOException {
		return memberClient.selectById(id, authHeader).execute().body();
	}

	public Member selectByEmail(String email, String authHeader)
			throws IOException {
		return memberClient.selectByEmail(email, authHeader).execute().body();
	}

	public Response<Void> insert(Member member, String authHeader) throws IOException {
		return memberClient.insert(member, authHeader).execute();
	}

	public Response<Void> update(Member member, String authHeader) throws IOException {
		return memberClient.update(member, authHeader).execute();
	}

	public Response<Void> delete(int id, String authHeader) throws IOException {
		return memberClient.delete(id, authHeader).execute();
	}
}
