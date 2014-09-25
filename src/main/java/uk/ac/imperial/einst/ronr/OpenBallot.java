package uk.ac.imperial.einst.ronr;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;

public class OpenBallot extends Action {

	final Motion motion;

	public OpenBallot(Actor actor, Motion m) {
		super(actor, m.session.inst);
		this.motion = m;
	}

	public Motion getMotion() {
		return motion;
	}

	@Override
	public String toString() {
		return "open_ballot(" + getActor() + ", " + motion + ")";
	}

}
