package dxc.assignment.controller;

import java.util.List;

import javax.ws.rs.DELETE;

import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dxc.assignment.mapper.MemberMapper;
import dxc.assignment.model.Member;
import dxc.assignment.model.response.MemberSelectResponse;

@RestController
@RequestMapping("/members")
public class MemberController {
	private final MemberMapper memberMapper;

	public MemberController(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}

	@GetMapping
	public MemberSelectResponse select(@RequestParam String searchString,
			@RequestParam int currentPage, @RequestParam int pageSize) {
		List<Member> members = memberMapper.select(searchString,
				new RowBounds(pageSize * (currentPage - 1), pageSize));
		int totalCount = memberMapper.countMembers(searchString);
		return new MemberSelectResponse(members, totalCount);
	}
	
	@GetMapping("id/{id}")
	public Member selectById(@PathVariable("id") int id) {
		return memberMapper.selectById(id);
	}
	
	@GetMapping("email/{email}")
	public Member selectById(@PathVariable("email") String email) {
		return memberMapper.selectByEmail(email);
	}

	@PostMapping
	public void insert(@RequestBody Member member) {
		memberMapper.insert(member);
	}
	
	@PutMapping
	public void update(@RequestBody Member member) {
		memberMapper.update(member);
	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") int id) {
		memberMapper.delete(id);
	}
}
