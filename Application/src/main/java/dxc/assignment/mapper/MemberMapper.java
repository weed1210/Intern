package dxc.assignment.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import dxc.assignment.model.Member;

@Mapper
public interface MemberMapper {
	public List<Member> select(String searchString, RowBounds rowBounds);
	
	public int countMembers(String searchString);

	public Member selectById(int id);
	
	public Member selectByEmail(String email);
	
	public void insert(Member member);
	
	public void update(Member member);
	
	public void delete(int id);
}
