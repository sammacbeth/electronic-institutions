package uk.ac.imperial.einst.ipower;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;

public class Pow {
	final Actor actor;
	final Action action;

	public Pow(Actor actor, Action action) {
		super();
		this.actor = actor;
		this.action = action;
	}

	public Actor getActor() {
		return actor;
	}

	public Action getAction() {
		return action;
	}

	public boolean matches(Action act) {
		return action.equalsIgnoreT(act);
	}

	@Override
	public String toString() {
		return "pow(" + actor + ", " + action + ")";
	}

}
