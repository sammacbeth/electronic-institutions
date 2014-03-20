package uk.ac.imperial.einst.resource;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import uk.ac.imperial.einst.Institution;

public class Pool {

	final Institution inst;
	final Set<String> contribRoles;
	final Set<String> extractRoles;
	final Set<String> removalRoles;
	final ArtifactMatcher artifactMatcher;
	final List<Object> artifacts = new LinkedList<Object>();

	public Pool(Institution inst, Set<String> contribRoles,
			Set<String> extractRoles, Set<String> removalRoles,
			ArtifactMatcher artifactMatcher) {
		super();
		this.inst = inst;
		this.contribRoles = contribRoles;
		this.extractRoles = extractRoles;
		this.removalRoles = removalRoles;
		this.artifactMatcher = artifactMatcher;
	}

	public Institution getInst() {
		return inst;
	}

	public Set<String> getContribRoles() {
		return contribRoles;
	}

	public Set<String> getExtractRoles() {
		return extractRoles;
	}

	public Set<String> getRemovalRoles() {
		return removalRoles;
	}

	public ArtifactMatcher getArtifactMatcher() {
		return artifactMatcher;
	}

	public List<Object> getArtifacts() {
		return artifacts;
	}

	public class Added {
		final Provision prov;
		final Pool pool;

		public Added(Provision prov) {
			super();
			this.prov = prov;
			this.pool = Pool.this;
		}

		public Provision getProv() {
			return prov;
		}

		public Pool getPool() {
			return pool;
		}

		@Override
		public String toString() {
			return "added(" + pool + ", " + prov + ")";
		}

	}

	@Override
	public String toString() {
		return "pool(" + inst + ", " + contribRoles + ", " + extractRoles
				+ ", " + artifactMatcher + ", " + artifacts.size()
				+ " artifacts)";
	}

}
