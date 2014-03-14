package uk.ac.imperial.einst.resource.facility;

import org.drools.runtime.StatefulKnowledgeSession;

import uk.ac.imperial.einst.EInstSession;
import uk.ac.imperial.einst.Module;
import uk.ac.imperial.einst.RuleResources;

@RuleResources("einst/facilities.drl")
public class Facilities implements Module {

	@Override
	public void initialise(EInstSession eInstSession,
			StatefulKnowledgeSession session) {
	}

}
