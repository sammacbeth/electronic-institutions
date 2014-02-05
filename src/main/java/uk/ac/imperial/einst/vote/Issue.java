package uk.ac.imperial.einst.vote;

import java.util.Arrays;
import java.util.Set;

import uk.ac.imperial.einst.Institution;

public class Issue {

	public final static Object[] MOTION_VOTE = new String[] { "aye", "nay",
			"abs" };

	final Institution inst;
	final String name;
	Set<String> cfvRoles;
	Set<String> voteRoles;
	VoteMethod method;
	Object[] voteValues;
	String wdm;

	public Issue(Institution inst, String name, Set<String> cfvRoles,
			Set<String> voteRoles, VoteMethod method, Object[] voteValues,
			String wdm) {
		super();
		this.inst = inst;
		this.name = name;
		this.cfvRoles = cfvRoles;
		this.voteRoles = voteRoles;
		this.method = method;
		this.voteValues = voteValues;
		this.wdm = wdm;
	}

	public String getName() {
		return name;
	}

	public Institution getInst() {
		return inst;
	}

	public Set<String> getCfvRoles() {
		return cfvRoles;
	}

	public Set<String> getVoteRoles() {
		return voteRoles;
	}

	public VoteMethod getMethod() {
		return method;
	}

	public Object[] getVoteValues() {
		return voteValues;
	}

	public String getWdm() {
		return wdm;
	}

	@Override
	public String toString() {
		return "issue(" + inst + ", " + name + ", " + cfvRoles + ", "
				+ voteRoles + ", " + method + ", "
				+ Arrays.toString(voteValues) + ", " + wdm + ")";
	}

}
