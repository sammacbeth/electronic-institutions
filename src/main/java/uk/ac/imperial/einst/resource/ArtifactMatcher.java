package uk.ac.imperial.einst.resource;

public interface ArtifactMatcher {

	public boolean matches(Object artifact);

	public static ArtifactMatcher ALL = new ArtifactMatcher() {

		@Override
		public boolean matches(Object artifact) {
			return true;
		}

		@Override
		public String toString() {
			return "all";
		}
	};

}
