package uk.ac.imperial.einst.micropay;

import org.drools.runtime.StatefulKnowledgeSession;

import uk.ac.imperial.einst.EInstSession;
import uk.ac.imperial.einst.Module;
import uk.ac.imperial.einst.RuleResources;

@RuleResources("einst/micropayments.drl")
public class MicroPayments implements Module {

	StatefulKnowledgeSession session;

	@Override
	public void initialise(EInstSession eInstSession,
			StatefulKnowledgeSession session) {
		this.session = session;
	}

	public boolean isCleared(Transfer t) {
		return session.getQueryResults("cleared", t).size() > 0;
	}

}
