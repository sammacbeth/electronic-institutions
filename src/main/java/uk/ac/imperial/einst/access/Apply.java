package uk.ac.imperial.einst.access;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;

public class Apply extends Action {

	final String role;

	public Apply(Actor actor, Institution inst, String role) {
		super(actor, inst);
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	@Override
	public String toString() {
		return "apply("+ getActor() +", "+ getInst() +", "+ role +")";
	}

}
