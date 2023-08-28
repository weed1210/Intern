package dxc.assignment.client;

import dxc.assignment.model.Member;
import dxc.assignment.model.response.MemberSelectResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MemberClient {
	@GET("members")
	Call<MemberSelectResponse> select(@Query("searchString") String searchString,
			@Query("currentPage") int currentPage, @Query("pageSize") int pageSize);

	@GET("members/id/{id}")
	Call<Member> selectById(@Path("id") int id);
	
	@GET("members/email/{email}")
	Call<Member> selectByEmail(@Path("email") String email);

	@POST("members")
	Call<Void> insert(@Body Member member);
	
	@PUT("members")
	Call<Void> update(@Body Member member);

	@DELETE("members/{id}")
	Call<Void> delete(@Path("id") int id);
}
