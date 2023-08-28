package dxc.assignment.model.response;

import java.util.List;

import dxc.assignment.model.Member;

public class MemberSelectResponse {
	private List<Member> members;
	private int totalCount;

	public MemberSelectResponse(List<Member> members, int totalCount) {
		super();
		this.members = members;
		this.totalCount = totalCount;
	}

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
