package uk.ac.imperial.einst.access;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.drools.runtime.ObjectFilter;
import org.drools.runtime.StatefulKnowledgeSession;

import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.EInstSession;
import uk.ac.imperial.einst.Institution;
import uk.ac.imperial.einst.Module;
import uk.ac.imperial.einst.RuleResources;

@RuleResources("einst/access.drl")
public class AccessControl implements Module {

	/**
	 * Constants for special roles
	 */
	public final static String GATEKEEPER = "gatekeeper";
	
	StatefulKnowledgeSession session;
	
	public AccessControl() {
		super();
	}

	@Override
	public void initialise(EInstSession eInstSession,
			StatefulKnowledgeSession session) {
		this.session = session;
	}
	
	public Set<String> getRoles(final Actor a, final Institution i) {
		final Set<String> roles = new HashSet<String>();
		Collection<Object> res = session.getObjects(new ObjectFilter() {
			@Override
			public boolean accept(Object object) {
				if (object instanceof RoleOf) {
					RoleOf r = (RoleOf) object;
					if (r.getActor().equals(a) && r.getInst().equals(i)) {
						return true;
					}
				}
				return false;
			}
		});
		for (Object o : res) {
			roles.add(((RoleOf) o).getRole());
		}
		return roles;
	}

}
