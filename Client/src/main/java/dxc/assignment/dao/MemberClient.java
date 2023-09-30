package dxc.assignment.dao;

import dxc.assignment.model.Member;
import dxc.assignment.model.response.MemberSelectResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MemberClient {
	@GET("members")
	Call<MemberSelectResponse> select(@Query("searchString") String searchString,
			@Query("currentPage") int currentPage, @Query("pageSize") int pageSize,
			@Header("Authorization") String authHeader);

	@GET("members/id/{id}")
	Call<Member> selectById(@Path("id") int id,
			@Header("Authorization") String authHeader);

	@GET("members/email/{email}")
	Call<Member> selectByEmail(@Path("email") String email,
			@Header("Authorization") String authHeader);
	
	@GET("members/super/{email}")
	Call<Member> selectSuperByEmail(@Path("email") String email,
			@Header("Authorization") String authHeader);

	@Headers({"Accept: application/json"})
	@POST("members")
	Call<Void> insert(@Body Member member, @Header("Authorization") String authHeader);

	@PUT("members")
	Call<Void> update(@Body Member member, @Header("Authorization") String authHeader);

	@DELETE("members/{id}")
	Call<Void> delete(@Path("id") int id, @Header("Authorization") String authHeader);
}
