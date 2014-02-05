package uk.ac.imperial.einst.vote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import uk.ac.imperial.einst.Actor;
import uk.ac.imperial.einst.Institution;
import uk.ac.imperial.einst.Module;
import uk.ac.imperial.einst.SpecificationTest;
import uk.ac.imperial.einst.StubActor;
import uk.ac.imperial.einst.StubInstitution;
import uk.ac.imperial.einst.UnavailableModuleException;
import uk.ac.imperial.einst.access.RoleOf;
import uk.ac.imperial.einst.ipower.IPower;
import uk.ac.imperial.einst.ipower.Obl;
import uk.ac.imperial.einst.ipower.Pow;

public class VoteTest extends SpecificationTest {

	@Override
	protected Iterable<Class<? extends Module>> getModulesUnderTest() {
		Set<Class<? extends Module>> mods = new HashSet<Class<? extends Module>>();
		mods.add(Voting.class);
		mods.add(IPower.class);
		return mods;
	}

	@Test
	public void testOpenCloseBallot() throws UnavailableModuleException {
		IPower ipow = session.getModule(IPower.class);
		Voting voting = session.getModule(Voting.class);

		Institution i = new StubInstitution("I");
		Actor h = new StubActor("H");
		Actor a = new StubActor("A");

		Set<String> cfvRoles = new HashSet<String>();
		cfvRoles.add("head");
		Set<String> voteRoles = new HashSet<String>();
		voteRoles.add("test");
		Issue issue = new Issue(i, "test", cfvRoles, voteRoles,
				VoteMethod.SINGLE_CANDIDATE, Issue.MOTION_VOTE, Plurality.NAME);

		session.insert(issue);
		session.insert(new RoleOf(h, i, "head"));
		session.insert(new RoleOf(a, i, "test"));
		session.incrementTime();

		assertTrue(ipow.getPowers(h).size() == 1);

		session.insert(new OpenBallot(h, i, issue));
		session.incrementTime();

		// check ballot open
		Ballot b = voting.getOpenBallot(issue);
		assertNotNull(b);
		assertEquals(Ballot.Status.OPEN, b.getStatus());
		assertEquals(issue, b.getIssue());

		// check pow close ballot
		List<Pow> powers = ipow.getPowers(h);
		assertTrue(powers.size() == 1);
		assertTrue(powers.get(0).getAction()
				.equalsIgnoreT(new CloseBallot(h, i, b)));

		session.insert(new CloseBallot(h, i, b));
		session.incrementTime();

		List<Obl> obls = ipow.getObligations(h);
		assertTrue(obls.size() == 1);
		assertTrue(obls.get(0).getAction()
				.equalsIgnoreT(new Declare(h, i, b, null)));

		session.insert(new Declare(h, i, b, null));
		session.incrementTime();

		obls = ipow.getObligations(h);
		assertTrue(obls.get(0).isDone());
	}

	@Test
	public void testVote() throws UnavailableModuleException {
		IPower ipow = session.getModule(IPower.class);
		Voting voting = session.getModule(Voting.class);

		Institution i = new StubInstitution("I");
		Actor h = new StubActor("H");
		Actor a = new StubActor("A");

		Set<String> cfvRoles = new HashSet<String>();
		cfvRoles.add("head");
		Set<String> voteRoles = new HashSet<String>();
		voteRoles.add("test");
		Issue issue = new Issue(i, "test", cfvRoles, voteRoles,
				VoteMethod.SINGLE_CANDIDATE, Issue.MOTION_VOTE, Plurality.NAME);

		session.insert(issue);
		session.insert(new RoleOf(h, i, "head"));
		session.insert(new RoleOf(a, i, "test"));
		session.incrementTime();

		assertTrue(ipow.getPowers(a).size() == 0);

		// open a ballot
		session.insert(new OpenBallot(h, i, issue));
		session.incrementTime();

		// check ballot is open and agent A has power to vote.
		Ballot b = voting.getOpenBallot(issue);
		assertTrue(ipow.getPowers(a).size() == 1);
		assertTrue(ipow.getPowers(a).get(0).getAction()
				.equalsIgnoreT(new Vote(a, i, b, null)));

		// submit one valid and one invalid vote
		Vote av = new Vote(a, i, b, "aye");
		Vote hv = new Vote(h, i, b, "nay");
		session.insert(av);
		session.insert(hv);
		session.incrementTime();
		
		// A no longer has vote power, votes validity correctly marked.
		assertTrue(ipow.getPowers(a).size() == 0);
		assertTrue(av.isValid());
		assertFalse(hv.isValid());
		
		// close ballot, count results
		session.insert(new CloseBallot(h, i, b));
		session.incrementTime();
		
		// H should have to declare motion has passed (1 aye vote)
		List<Obl> obls = ipow.getObligations(h);
		assertTrue(obls.size() == 1);
		assertTrue(obls.get(0).getAction()
				.equalsIgnoreT(new Declare(h, i, b, "aye")));
		
		session.insert(new Declare(h, i, b, "aye"));
		session.incrementTime();

		obls = ipow.getObligations(h);
		assertTrue(obls.get(0).isDone());
	}

}
