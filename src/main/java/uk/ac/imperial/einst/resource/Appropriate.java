package uk.ac.imperial.einst.resource;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;

public class Appropriate extends Action {

	final Object artifact;

	public Appropriate(Actor actor, Institution inst, Object artifact) {
		super(actor, inst);
		this.artifact = artifact;
	}

	public Object getArtifact() {
		return artifact;
	}

	@Override
	public String toString() {
		return "appropriate(" + getActor() + ", " + getInst() + ", " + artifact
				+ ")";
	}

}
