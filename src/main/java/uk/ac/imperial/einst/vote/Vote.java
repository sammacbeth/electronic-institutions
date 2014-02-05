package uk.ac.imperial.einst.vote;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;

public class Vote extends Action {
	final Ballot ballot;
	final Object vote;

	public Vote(Actor actor, Institution inst, Ballot ballot, Object vote) {
		super(actor, inst);
		this.ballot = ballot;
		this.vote = vote;
	}

	public Ballot getBallot() {
		return ballot;
	}

	public Object getVote() {
		return vote;
	}

	@Override
	public String toString() {
		return "vote(" + getActor() +", " + getInst() + ", "+ ballot + ", " + vote + ")";
	}

}
