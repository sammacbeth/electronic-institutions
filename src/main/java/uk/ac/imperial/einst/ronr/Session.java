package uk.ac.imperial.einst.ronr;

import java.util.HashSet;
import java.util.Set;

import uk.ac.imperial.einst.Institution;

public class Session {

	final Institution inst;
	final String name;
	boolean sitting;
	Set<Motion> carried = new HashSet<Motion>();
	Set<Motion> notCarried = new HashSet<Motion>();

	public Session(Institution inst, String name, boolean sitting) {
		super();
		this.inst = inst;
		this.name = name;
		this.sitting = sitting;
	}

	public Institution getInst() {
		return inst;
	}

	public String getName() {
		return name;
	}

	public boolean isSitting() {
		return sitting;
	}

	public void setSitting(boolean sitting) {
		this.sitting = sitting;
	}

	public Set<Motion> getCarried() {
		return carried;
	}

	public void addCarried(Motion m) {
		this.carried.add(m);
	}

	public Set<Motion> getNotCarried() {
		return notCarried;
	}

	public void addNotCarried(Motion m) {
		this.notCarried.add(m);
	}

	@Override
	public String toString() {
		return "session(" + inst + ", " + name + ", "
				+ (sitting ? "sitting" : "closed") + ", [" + carried + ", "
				+ notCarried + "])";
	}

}
