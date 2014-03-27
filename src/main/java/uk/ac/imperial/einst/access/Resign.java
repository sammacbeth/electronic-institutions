package uk.ac.imperial.einst.access;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;

public class Resign extends Action {

	final String role;

	public Resign(Actor actor, Institution inst, String role) {
		super(actor, inst);
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	@Override
	public String toString() {
		return "resign(" + getActor() + ", " + getInst() + ", " + role + ")";
	}
}
