package uk.ac.imperial.einst.vote;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;

public class OpenBallot extends Action {

	final Issue issue;

	public OpenBallot(Actor actor, Institution inst, Issue issue) {
		super(actor, inst);
		this.issue = issue;
	}

	public Issue getIssue() {
		return issue;
	}

	@Override
	public String toString() {
		return "openBallot(" + getActor() + ", " + getInst() + ", "
				+ issue.getName() + ")";
	}

}
