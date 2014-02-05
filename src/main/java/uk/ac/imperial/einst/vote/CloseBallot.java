package uk.ac.imperial.einst.vote;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;

public class CloseBallot extends Action {
	final Ballot ballot;

	public CloseBallot(Actor actor, Institution inst, Ballot ballot) {
		super(actor, inst);
		this.ballot = ballot;
	}

	public Ballot getBallot() {
		return ballot;
	}

	@Override
	public String toString() {
		return "closeBallot(" + ballot + ")";
	}
}
