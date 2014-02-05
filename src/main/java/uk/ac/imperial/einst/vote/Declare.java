package uk.ac.imperial.einst.vote;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;

public class Declare extends Action {
	final Ballot ballot;
	final Object winner;

	public Declare(Actor actor, Institution inst, Ballot ballot, Object winner) {
		super(actor, inst);
		this.ballot = ballot;
		this.winner = winner;
	}

	public Ballot getBallot() {
		return ballot;
	}

	public Object getWinner() {
		return winner;
	}

	@Override
	public String toString() {
		return "declare(" + ballot + ", " + winner + ")";
	}

}
