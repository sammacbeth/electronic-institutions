package uk.ac.imperial.einst.access;

import uk.ac.imperial.einst.Actor;

public class Qualifies {
	final Actor actor;
	final String role;

	public Qualifies(Actor actor, String role) {
		super();
		this.actor = actor;
		this.role = role;
	}

	public Actor getActor() {
		return actor;
	}

	public String getRole() {
		return role;
	}

	@Override
	public String toString() {
		return "qualifies(" + actor + ", " + role + ")";
	}

}
