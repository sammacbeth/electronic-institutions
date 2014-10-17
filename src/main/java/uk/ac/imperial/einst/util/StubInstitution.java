package uk.ac.imperial.einst.util;

import uk.ac.imperial.einst.Institution;

public class StubInstitution implements Institution {

	final String name;

	public StubInstitution(String name) {
		super();
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
