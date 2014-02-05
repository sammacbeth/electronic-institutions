package uk.ac.imperial.einst.vote;

import java.util.HashSet;
import java.util.Set;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.QueryResults;
import org.drools.runtime.rule.QueryResultsRow;
import org.drools.runtime.rule.Variable;

import uk.ac.imperial.einst.EInstSession;
import uk.ac.imperial.einst.Institution;
import uk.ac.imperial.einst.Module;
import uk.ac.imperial.einst.RuleResources;

@RuleResources("einst/voting.drl")
public class Voting implements Module {

	EInstSession einst;
	StatefulKnowledgeSession session;

	public Voting() {
		super();
	}

	@Override
	public void initialise(EInstSession eInstSession,
			StatefulKnowledgeSession session) {
		this.einst = eInstSession;
		this.session = session;
	}

	public Set<Issue> getIssues(Institution inst) {
		QueryResults res = session.getQueryResults("getIssues", inst);
		Set<Issue> issues = new HashSet<Issue>();
		for (QueryResultsRow row : res) {
			issues.add((Issue) row.get("issue"));
		}
		return issues;
	}

	public Set<Ballot> getOpenBallots(Institution inst) {
		QueryResults res = session.getQueryResults("getOpenBallot", inst,
				Variable.v);
		Set<Ballot> ballots = new HashSet<Ballot>();
		for (QueryResultsRow row : res) {
			ballots.add((Ballot) row.get("ballot"));
		}
		return ballots;
	}

	public Ballot getOpenBallot(Issue issue) {
		QueryResults res = session.getQueryResults("getOpenBallot",
				issue.getInst(), issue);
		if (res.size() == 1) {
			return (Ballot) res.iterator().next().get("ballot");
		}
		return null;
	}

}
