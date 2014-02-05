package uk.ac.imperial.einst.ipower;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;

public class Obl {
	final Actor actor;
	final Action action;
	boolean done = false;

	public Obl(Actor actor, Action action) {
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

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public boolean matches(Action act) {
		return action.equalsIgnoreT(act);
	}

	@Override
	public String toString() {
		return "obl(" + actor + ", " + action + ")";
	}
}
