package uk.ac.imperial.einst.resource;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.QueryResultsRow;
import org.drools.runtime.rule.Row;
import org.drools.runtime.rule.ViewChangedEventListener;

import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.EInstSession;
import uk.ac.imperial.einst.Institution;
import uk.ac.imperial.einst.Module;
import uk.ac.imperial.einst.RuleResources;
import uk.ac.imperial.einst.ipower.Pow;

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

	public void registerForAppropriations(Actor a,
			AppropriationsListener listener) {
		einst.getQueries().add(
				session.openLiveQuery("getAppropriations", new Object[] { a },
						new AppListener(listener)));
	}

	public List<Pow> getPowProvision(Actor a) {
		List<Pow> pows = new LinkedList<Pow>();
		for (QueryResultsRow row : session
				.getQueryResults("getPowProvision", a)) {
			pows.add((Pow) row.get("act"));
		}
		return pows;
	}
	
	public PoolUsage getPoolUsage(Actor a, Pool p, int since) {
		Iterator<QueryResultsRow> it = session.getQueryResults("getPoolUsage", a, p, since).iterator();
		if(it.hasNext()) {
			QueryResultsRow row = it.next();
			return new PoolUsage(Integer.parseInt(row.get("provCount").toString()), Integer.parseInt(row.get("appCount").toString()));
		} else {
			return new PoolUsage(0, 0);
		}
	}

	class AppListener implements ViewChangedEventListener {
		final AppropriationsListener client;

		public AppListener(AppropriationsListener client) {
			super();
			this.client = client;
		}

		@Override
		public void rowAdded(Row row) {
			client.onAppropriation(row.get("item"),
					(Institution) row.get("inst"));
		}

		@Override
		public void rowRemoved(Row row) {
		}

		@Override
		public void rowUpdated(Row row) {
		}
	}

	static public class PoolUsage {
		final public int provisions;
		final public int appropriations;

		public PoolUsage(int provisions, int appropriations) {
			super();
			this.provisions = provisions;
			this.appropriations = appropriations;
		}

	}

}
