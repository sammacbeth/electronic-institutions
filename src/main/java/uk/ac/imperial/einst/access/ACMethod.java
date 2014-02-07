package uk.ac.imperial.einst.access;

import uk.ac.imperial.einst.Institution;

public class ACMethod {

	public final static String DISCRETIONARY = "discretionary";
	public final static String NONE = "none";

	final Institution inst;
	final String role;
	final String method;

	public ACMethod(Institution inst, String role, String method) {
		super();
		this.inst = inst;
		this.role = role;
		this.method = method;
	}

	public Institution getInst() {
		return inst;
	}

	public String getRole() {
		return role;
	}

	public String getMethod() {
		return method;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inst == null) ? 0 : inst.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ACMethod other = (ACMethod) obj;
		if (inst == null) {
			if (other.inst != null)
				return false;
		} else if (!inst.equals(other.inst))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "acMethod(" + inst + ", " + role + ") = " + method + "";
	}

}
