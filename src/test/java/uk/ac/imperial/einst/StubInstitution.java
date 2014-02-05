package uk.ac.imperial.einst;

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
