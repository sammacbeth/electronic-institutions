package uk.ac.imperial.einst;

import org.drools.runtime.StatefulKnowledgeSession;

/**
 * A module can provide two things to an Electronic Institution session: 1)
 * Drools rules files to be loaded (specified with the {@link RuleResources}
 * annotation) and 2) a set of method for access and manipulation of drools
 * session state.
 * 
 * A module implementation must specify a zero-argument constructor in order for
 * the {@link EInstSession} to be able to instantiate it.
 * 
 * @author Sam Macbeth
 * 
 */
public interface Module {

	void initialise(EInstSession eInstSession, StatefulKnowledgeSession session);

}
