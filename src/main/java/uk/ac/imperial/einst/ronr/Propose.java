package uk.ac.imperial.einst.ronr;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;

public class Propose extends Action {

	final Motion motion;

	public Propose(Actor actor, Motion motion) {
		super(actor, motion.session.inst);
		this.motion = motion;
	}

	public Motion getMotion() {
		return motion;
	}

	@Override
	public String toString() {
		return "propose(" + getActor() + ", " + motion + ")";
	}

}
