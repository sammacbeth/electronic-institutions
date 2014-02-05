package uk.ac.imperial.einst;

import org.drools.runtime.StatefulKnowledgeSession;

public interface Module {

	void initialise(EInstSession eInstSession, StatefulKnowledgeSession session);

}
