package uk.ac.imperial.einst.resource;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;

public class Prune extends Action {

	final ArtifactMatcher query;
	final int before;
	final int limit;

	public Prune(Actor actor, Institution inst, ArtifactMatcher query,
			int before, int limit) {
		super(actor, inst);
		this.query = query;
		this.before = before;
		this.limit = limit;
	}

	public ArtifactMatcher getQuery() {
		return query;
	}

	public int getBefore() {
		return before;
	}

	public int getLimit() {
		return limit;
	}

	@Override
	public String toString() {
		return "prune(" + getActor() + ", " + getInst() + ", " + query + ", "
				+ before + ", " + limit + ")" + toStringSuffix();
	}
}
