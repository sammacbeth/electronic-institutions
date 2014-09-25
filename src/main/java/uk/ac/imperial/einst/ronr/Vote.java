package uk.ac.imperial.einst.ronr;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;

public class Vote extends Action {

	public enum Choice {
		NAY, AYE
	};

	final Motion motion;
	final Choice vote;

	public Vote(Actor actor, Motion m, Choice vote) {
		super(actor, m.session.inst);
		this.motion = m;
		this.vote = vote;
	}

	public Motion getMotion() {
		return motion;
	}

	public Choice getVote() {
		return vote;
	}

	@Override
	public String toString() {
		return "vote(" + getActor() + ", " + motion + ", " + vote + ")";
	}

}
