package uk.ac.imperial.einst.ronr;

import uk.ac.imperial.einst.Inspectable;

@Inspectable
public class Motion {

	public enum Status {
		Null, Proposed, Seconded, Voting, Voted, Carried, NotCarried
	};

	final Session session;
	final String name;
	Status status;
	int voting = -1;
	int ayes = 0;
	int nays = 0;

	public Motion(Session session, String name) {
		super();
		this.session = session;
		this.name = name;
		this.status = Status.Null;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getVoting() {
		return voting;
	}

	public void setVoting(int voting) {
		this.voting = voting;
	}

	public Session getSession() {
		return session;
	}

	public String getName() {
		return name;
	}
	
	public void addAye() {
		ayes++;
	}
	
	public void addNay() {
		nays++;
	}

	@Override
	public String toString() {
		return "motion(" + name + ", " + status + ")";
	}
}
