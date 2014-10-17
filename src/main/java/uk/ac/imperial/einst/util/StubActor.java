package uk.ac.imperial.einst.util;

import uk.ac.imperial.einst.Actor;

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
