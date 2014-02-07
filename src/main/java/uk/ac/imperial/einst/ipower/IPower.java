package uk.ac.imperial.einst.ipower;

import java.util.LinkedList;
import java.util.List;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.LiveQuery;
import org.drools.runtime.rule.QueryResults;
import org.drools.runtime.rule.QueryResultsRow;

import uk.ac.imperial.einst.Action;
import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.EInstSession;
import uk.ac.imperial.einst.Module;
import uk.ac.imperial.einst.RuleResources;

/**
 * Implementation of Institutionalised Power. This module just deals with access
 * to powers, permissions and obligations facts. It depends on other modules
 * correctly utilising the {@link Pow} and {@link Obl} objects with drools.
 * 
 * @author Sam Macbeth
 * 
 */
@RuleResources("einst/ipower.drl")
public class IPower implements Module {

	StatefulKnowledgeSession session;
	List<LiveQuery> queries = new LinkedList<LiveQuery>();

	public IPower() {
		super();
	}

	@Override
	public void initialise(EInstSession eInstSession,
			StatefulKnowledgeSession session) {
		this.session = session;
	}

	public List<Pow> getPowers(Actor a) {
		List<Pow> pows = new LinkedList<Pow>();
		QueryResults res = session.getQueryResults("allPows", a);
		for (QueryResultsRow row : res) {
			pows.add((Pow) row.get("pow"));
		}
		return pows;
	}

	public List<Obl> getObligations(Actor a) {
		List<Obl> obls = new LinkedList<Obl>();
		QueryResults res = session.getQueryResults("obl", a);
		for (QueryResultsRow row : res) {
			obls.add((Obl) row.get("obl"));
		}
		return obls;
	}

	public void registerObligationReactive(ObligationReactive r, Actor a) {
		queries.add(session.openLiveQuery("obl", new Object[] { a },
				new ObligationListener(r)));
	}

	public boolean pow(Actor a, Action act) {
		return session.getQueryResults("pow", a, act).size() > 0;
	}

}
