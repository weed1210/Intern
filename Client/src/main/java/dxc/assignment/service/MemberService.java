package dxc.assignment.service;

import java.util.Collections;
import java.util.List;

import javax.sound.midi.Soundbank;

import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dxc.assignment.mapper.MemberMapper;
import dxc.assignment.model.Member;

@Service
public class MemberService {
	private int pageSize = 10;
	private final MemberMapper memberMapper;

	public MemberService(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}

	public Page<Member> select(String searchString, int currentPage) {
		List<Member> members = memberMapper.select(searchString,
				new RowBounds(pageSize * (currentPage - 1), pageSize));

		Page<Member> paginatedMember = new PageImpl<Member>(members,
				PageRequest.of(currentPage-1, pageSize),
				memberMapper.countMembers(searchString));

		return paginatedMember;
	}

	public Member selectById(int id) {
		return memberMapper.selectById(id);
	}

	public Member selectByEmail(String email) {
		return memberMapper.selectByEmail(email);
	}

	public void insert(Member member) {
		memberMapper.insert(member);
	}

	public void update(Member member) {
		memberMapper.update(member);
	}

	public void delete(int id) {
		memberMapper.delete(id);
	}
}
