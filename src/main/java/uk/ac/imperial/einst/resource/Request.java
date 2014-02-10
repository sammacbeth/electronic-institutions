package uk.ac.imperial.einst.resource;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;

public class Request extends Action {

	final ArtifactMatcher query;
	final int limit;

	public Request(Actor actor, Institution inst, ArtifactMatcher query,
			int limit) {
		super(actor, inst);
		this.query = query;
		this.limit = limit;
	}

	public ArtifactMatcher getQuery() {
		return query;
	}

	public int getLimit() {
		return limit;
	}

	@Override
	public String toString() {
		return "request(" + getActor() + ", " + getInst() + ", " + query + ", "
				+ limit + ")" + toStringSuffix();
	}

}
