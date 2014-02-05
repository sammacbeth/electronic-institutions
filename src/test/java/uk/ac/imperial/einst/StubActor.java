package uk.ac.imperial.einst;

public class StubActor implements Actor {

	final String name;

	public StubActor(String name) {
		super();
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
