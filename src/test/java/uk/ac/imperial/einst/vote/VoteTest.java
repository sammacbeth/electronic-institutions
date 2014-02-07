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
				VoteMethod.SINGLE, Issue.MOTION_VOTE, Plurality.NAME);

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
				VoteMethod.SINGLE, Issue.MOTION_VOTE, Plurality.NAME);

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

	@Test
	public void testBorda() throws UnavailableModuleException {
		IPower ipow = session.getModule(IPower.class);
		Voting voting = session.getModule(Voting.class);

		Institution i = new StubInstitution("I");
		Actor h = new StubActor("H");
		Actor a = new StubActor("A");

		Set<String> cfvRoles = new HashSet<String>();
		cfvRoles.add("head");
		Set<String> voteRoles = new HashSet<String>();
		voteRoles.add("test");
		voteRoles.add("head");
		String[] voteOptions = new String[] { "a", "b", "c" };
		Issue issue = new Issue(i, "test", cfvRoles, voteRoles,
				VoteMethod.RANK_ORDER, voteOptions, Borda.NAME);

		session.insert(issue);
		session.insert(new RoleOf(h, i, "head"));
		session.insert(new RoleOf(a, i, "test"));
		session.incrementTime();

		// open a ballot
		session.insert(new OpenBallot(h, i, issue));
		session.incrementTime();

		Ballot b = voting.getOpenBallot(issue);
		// submit one valid and one invalid vote
		Vote av = new Vote(a, i, b, new Preferences("a", "b", "c"));
		Vote hv = new Vote(h, i, b, new Preferences("b", "c", "a"));
		session.insert(av);
		session.insert(hv);
		session.incrementTime();

		// close ballot, count results
		session.insert(new CloseBallot(h, i, b));
		session.incrementTime();

		// H should have to declare b as winner
		List<Obl> obls = ipow.getObligations(h);
		assertTrue(obls.size() == 1);
		assertTrue(obls.get(0).getAction()
				.equalsIgnoreT(new Declare(h, i, b, "b")));

		session.insert(new Declare(h, i, b, "b"));
		session.incrementTime();

		obls = ipow.getObligations(h);
		assertTrue(obls.get(0).isDone());
	}

	@Test
	public void testJudgementAggregation() throws UnavailableModuleException {
		// Scenario from Pitt, Schaumeier, D'agostino,
		// "Modelling Resource Allocation in Open Embedded Systems: Some Logical and Epistemological Issues"
		Voting voting = session.getModule(Voting.class);
		IPower ipow = session.getModule(IPower.class);

		Institution i = new StubInstitution("I");
		Actor head = new StubActor("H");
		session.insert(new RoleOf(head, i, "head"));
		// 7 voting agents
		String[] voterNames = new String[] { "A", "B", "C", "D", "E", "F", "G" };
		Actor[] voters = new Actor[7];
		for (int j = 0; j < voters.length; j++) {
			voters[j] = new StubActor(voterNames[j]);
			session.insert(new RoleOf(voters[j], i, "voter"));
		}

		// voting roles
		Set<String> cfvRoles = new HashSet<String>();
		cfvRoles.add("head");
		Set<String> voteRoles = new HashSet<String>();
		voteRoles.add("voter");
		String[] voteOptions = new String[] { "a", "b", "c", "d", "e", "f" };

		// create issues to vote on
		Issue plurality = new Issue(i, "plurality", cfvRoles, voteRoles,
				VoteMethod.SINGLE, voteOptions, Plurality.NAME);
		Issue borda = new Issue(i, "borda", cfvRoles, voteRoles,
				VoteMethod.RANK_ORDER, voteOptions, Borda.NAME);
		session.insert(plurality);
		session.insert(borda);

		session.incrementTime();

		// open ballots
		session.insert(new OpenBallot(head, i, plurality));
		session.insert(new OpenBallot(head, i, borda));
		session.incrementTime();

		Ballot bPlurality = voting.getOpenBallot(plurality);
		Ballot bBorda = voting.getOpenBallot(borda);

		// voting
		session.insert(new Vote(voters[0], i, bPlurality, "a"));
		session.insert(new Vote(voters[1], i, bPlurality, "a"));
		session.insert(new Vote(voters[2], i, bPlurality, "a"));
		session.insert(new Vote(voters[3], i, bPlurality, "b"));
		session.insert(new Vote(voters[4], i, bPlurality, "b"));
		session.insert(new Vote(voters[5], i, bPlurality, "c"));
		session.insert(new Vote(voters[6], i, bPlurality, "d"));

		session.insert(new Vote(voters[0], i, bBorda, new Preferences("a", "b",
				"c", "e")));
		session.insert(new Vote(voters[1], i, bBorda, new Preferences("a", "d",
				"b", "e")));
		session.insert(new Vote(voters[2], i, bBorda, new Preferences("a", "c",
				"d", "e")));
		session.insert(new Vote(voters[3], i, bBorda, new Preferences("b", "c",
				"f", "e")));
		session.insert(new Vote(voters[4], i, bBorda, new Preferences("b", "d",
				"c", "e")));
		session.insert(new Vote(voters[5], i, bBorda, new Preferences("c", "d",
				"e", "b")));
		session.insert(new Vote(voters[6], i, bBorda, new Preferences("d", "c",
				"e", "b")));

		session.incrementTime();

		// close ballot, count results
		session.insert(new CloseBallot(head, i, bPlurality));
		session.insert(new CloseBallot(head, i, bBorda));
		session.incrementTime();

		session.insert(new Declare(head, i, bPlurality, "a"));
		session.insert(new Declare(head, i, bBorda, "c"));
		session.incrementTime();

		List<Obl> obls = ipow.getObligations(head);
		for (Obl ob : obls) {
			assertTrue(ob.isDone());
		}
	}

}
