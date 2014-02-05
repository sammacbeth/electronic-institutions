package uk.ac.imperial.einst.vote;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import uk.ac.imperial.einst.Actor;

public class Ballot {

	public enum Status {
		OPEN, CLOSED
	};

	Status status = Status.OPEN;
	final Issue issue;
	final int started;
	final Set<String> voteRoles;
	final Object[] options;
	final String wdm;
	Actor closedBy = null;

	public Ballot(Issue issue, int t) {
		super();
		this.issue = issue;
		this.started = t;
		this.voteRoles = new HashSet<String>(issue.voteRoles);
		this.options = issue.voteValues;
		this.wdm = this.issue.wdm;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Issue getIssue() {
		return issue;
	}

	public Set<String> getVoteRoles() {
		return voteRoles;
	}

	public Object[] getOptions() {
		return options;
	}

	public String getWdm() {
		return wdm;
	}

	public Actor getClosedBy() {
		return closedBy;
	}

	public void setClosedBy(Actor closedBy) {
		this.closedBy = closedBy;
	}

	public int getStarted() {
		return started;
	}

	@Override
	public String toString() {
		return "ballot(" + status + ", " + issue.getInst() + ", "
				+ issue.getName() + ", " + started + ", " + voteRoles + ", "
				+ Arrays.toString(options) + ", " + wdm + ")";
	}

}
