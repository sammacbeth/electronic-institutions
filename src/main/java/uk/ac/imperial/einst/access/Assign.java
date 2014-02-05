package uk.ac.imperial.einst.access;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;

public class Assign extends Action {

	final Actor target;
	final String role;

	public Assign(Actor actor, Actor target, Institution inst, String role) {
		super(actor, inst);
		this.target = target;
		this.role = role;
	}

	public Actor getTarget() {
		return target;
	}

	public String getRole() {
		return role;
	}

	@Override
	public String toString() {
		return "assign(" + getActor() + ", " + getTarget() + ", " + getInst()
				+ ", " + role + ")";
	}

}
