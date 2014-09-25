package uk.ac.imperial.einst.ronr;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.ronr.Motion.Status;

public class Declare extends Action {

	final Motion motion;
	final Motion.Status status;

	public Declare(Actor actor, Motion m, Status status) {
		super(actor, m.session.inst);
		this.motion = m;
		this.status = status;
	}

	public Motion getMotion() {
		return motion;
	}

	public Motion.Status getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "declare(" + getActor() + ", " + motion + ", " + status + ")";
	}

}
