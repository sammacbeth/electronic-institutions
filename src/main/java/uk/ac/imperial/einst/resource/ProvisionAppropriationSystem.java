package uk.ac.imperial.einst.resource;

import org.drools.runtime.StatefulKnowledgeSession;

import uk.ac.imperial.einst.EInstSession;
import uk.ac.imperial.einst.Module;
import uk.ac.imperial.einst.RuleResources;

@RuleResources("einst/provapp.drl")
public class ProvisionAppropriationSystem implements Module {

	public ProvisionAppropriationSystem() {
		super();
	}

	@Override
	public void initialise(EInstSession eInstSession,
			StatefulKnowledgeSession session) {

	}

}
