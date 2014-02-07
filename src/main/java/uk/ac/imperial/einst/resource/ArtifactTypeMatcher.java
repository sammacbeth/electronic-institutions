package uk.ac.imperial.einst.resource;

public class ArtifactTypeMatcher implements ArtifactMatcher {

	final Class<?> type;

	public ArtifactTypeMatcher(Class<?> type) {
		super();
		this.type = type;
	}

	@Override
	public boolean matches(Object artifact) {
		return type.isInstance(artifact);
	}

	/**
	 * Non-standard equals - does not meet equals contract. This is a hack for
	 * Provision equivalence.
	 */
	@Override
	public boolean equals(Object obj) {
		return matches(obj);
	}

	@Override
	public String toString() {
		return "type(" + type.getSimpleName() + ")";
	}

}
