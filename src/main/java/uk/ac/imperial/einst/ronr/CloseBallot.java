package uk.ac.imperial.einst.ronr;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;

public class CloseBallot extends Action {

	final Motion motion;

	public CloseBallot(Actor actor, Motion m) {
		super(actor, m.session.inst);
		this.motion = m;
	}

	public Motion getMotion() {
		return motion;
	}

	@Override
	public String toString() {
		return "close_ballot(" + getActor() + ", " + motion + ")";
	}

}
