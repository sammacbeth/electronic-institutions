package uk.ac.imperial.einst.vote;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;

public class Vote extends Action {
	final Ballot ballot;
	final Object vote;
	double weight = 1.0;

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
		return "vote(" + getActor() + ", " + getInst() + ", " + ballot.getIssue().getName() + ", "
				+ vote + ")" + toStringSuffix();
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

}
