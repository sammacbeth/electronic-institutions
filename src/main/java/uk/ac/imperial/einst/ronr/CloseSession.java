package uk.ac.imperial.einst.ronr;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;

public class CloseSession extends Action {

	final String name;

	public CloseSession(Actor actor, Institution inst, String name) {
		super(actor, inst);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "close_session(" + getActor() + ", " + name + ")";
	}

}
