package uk.ac.imperial.einst.ronr;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;

public class OpenSession extends Action {

	final String name;
	
	public OpenSession(Actor actor, Institution inst, String sessionName) {
		super(actor, inst);
		this.name = sessionName;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "open_session("+ getActor() +", " + name + ")";
	}

}
