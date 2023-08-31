package dxc.assignment.controller;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import dxc.assignment.model.error.ApiError;
import dxc.assignment.model.response.MemberSelectResponse;

@RestController
@RequestMapping(value = "/members", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {
	private final MemberMapper memberMapper;

	public MemberController(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}

	@GetMapping
	public MemberSelectResponse select(@RequestParam String searchString,
			@RequestParam int currentPage, @RequestParam int pageSize) {
		// Get members for specified page and search string
		List<Member> members = memberMapper.select(searchString,
				new RowBounds(pageSize * (currentPage - 1), pageSize));
		// Get total count for search string
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
	public ResponseEntity<Object> insert(@RequestBody Member member) {
		try {
			memberMapper.insert(member);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			PSQLException sqlEx = (PSQLException) e.getCause();
			ApiError error = new ApiError(sqlEx.getMessage(), HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(error, error.getStatus());
		}
	}

	@PutMapping
	public ResponseEntity<Object> update(@RequestBody Member member) {
		try {
			memberMapper.update(member);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			PSQLException sqlEx = (PSQLException) e.getCause();
			ApiError error = new ApiError(sqlEx.getMessage(), HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(error, error.getStatus());
		}
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Object> delete(@PathVariable("id") int id) {
		try {
			memberMapper.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			PSQLException sqlEx = (PSQLException) e.getCause();
			ApiError error = new ApiError(sqlEx.getMessage(), HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(error, error.getStatus());
		}
	}
}
