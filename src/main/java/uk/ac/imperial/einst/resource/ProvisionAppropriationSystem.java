package uk.ac.imperial.einst.resource;

import java.util.LinkedList;
import java.util.List;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.QueryResultsRow;

import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.EInstSession;
import uk.ac.imperial.einst.Module;
import uk.ac.imperial.einst.RuleResources;

@RuleResources("einst/provapp.drl")
public class ProvisionAppropriationSystem implements Module {

	EInstSession einst;
	StatefulKnowledgeSession session;

	public ProvisionAppropriationSystem() {
		super();
	}

	@Override
	public void initialise(EInstSession eInstSession,
			StatefulKnowledgeSession session) {
		this.einst = eInstSession;
		this.session = session;
	}

	public List<Object> getAppropriations(Actor a) {
		List<Object> apps = new LinkedList<Object>();
		for (QueryResultsRow row : session.getQueryResults("getAppropriations",
				a)) {
			apps.add(row.get("item"));
		}
		return apps;
	}

}
