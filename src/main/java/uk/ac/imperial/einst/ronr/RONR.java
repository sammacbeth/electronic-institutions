package uk.ac.imperial.einst.ronr;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.QueryResults;

import uk.ac.imperial.einst.EInstSession;
import uk.ac.imperial.einst.Institution;
import uk.ac.imperial.einst.Module;
import uk.ac.imperial.einst.RuleResources;

@RuleResources("einst/ronr.drl")
public class RONR implements Module {

	EInstSession einst;
	StatefulKnowledgeSession session;

	public RONR() {
		super();
	}

	@Override
	public void initialise(EInstSession eInstSession,
			StatefulKnowledgeSession session) {
		this.einst = eInstSession;
		this.session = session;
	}

	public Session getSession(Institution i, String name) {
		QueryResults res = session.getQueryResults("getSession", i, name);
		if (res.size() == 1) {
			return (Session) res.iterator().next().get("session");
		}
		return null;
	}

	public Motion getMotion(Institution i, String sesh, String name) {
		QueryResults res = session.getQueryResults("getMotion", i, sesh, name);
		if (res.size() == 1) {
			return (Motion) res.iterator().next().get("motion");
		}
		return null;
	}

}
