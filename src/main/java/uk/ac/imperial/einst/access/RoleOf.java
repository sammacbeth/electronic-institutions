package uk.ac.imperial.einst.access;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;

public class RoleOf {

	final Actor actor;
	final Institution inst;
	final String role;

	public RoleOf(Actor actor, Institution inst, String role) {
		super();
		this.actor = actor;
		this.inst = inst;
		this.role = role;
	}

	public Actor getActor() {
		return actor;
	}

	public Institution getInst() {
		return inst;
	}

	public String getRole() {
		return role;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actor == null) ? 0 : actor.hashCode());
		result = prime * result + ((inst == null) ? 0 : inst.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		RoleOf other = (RoleOf) obj;
		if (actor == null) {
			if (other.actor != null)
				return false;
		} else if (!actor.equals(other.actor))
			return false;
		if (inst == null) {
			if (other.inst != null)
				return false;
		} else if (!inst.equals(other.inst))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "role_of(" + actor + ", " + inst + ", " + role + ")";
	}

	public static Set<String> roleSet(String... roles) {
		Set<String> roleSet = new HashSet<String>();
		for (String r : roles) {
			roleSet.add(r);
		}
		return Collections.unmodifiableSet(roleSet);
	}

}
